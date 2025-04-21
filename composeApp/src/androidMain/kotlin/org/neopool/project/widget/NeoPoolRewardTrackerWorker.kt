package org.neopool.project.widget

import android.content.Context
import androidx.glance.appwidget.updateAll
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.neopool.project.poolreward.data.repository.PoolRepository
import java.time.Duration

class NeoPoolRewardTrackerWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), KoinComponent {

    companion object {

        const val UNIQUE_WORK_NAME = "pool_widget_update"

        fun scheduleUpdates(context: Context) {
            val workManager = WorkManager.getInstance(context)
            val updateRequest = PeriodicWorkRequestBuilder<NeoPoolRewardTrackerWorker>(
                Duration.ofMinutes(60),
            ).build()

            workManager.enqueueUniquePeriodicWork(
                "pool_widget_update",
                ExistingPeriodicWorkPolicy.UPDATE,
                updateRequest
            )
        }


        fun requestUpdate(context: Context) {
            val workManager = WorkManager.getInstance(context)
            val oneTimeUpdateRequest = OneTimeWorkRequestBuilder<NeoPoolRewardTrackerWorker>()
                .build()

            workManager.enqueue(oneTimeUpdateRequest)
        }
    }

    private val poolRepository: PoolRepository by inject()

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val poolResult = poolRepository.getPoolData()

            if (poolResult != NeoPoolRewardTrackerDataStore.poolData.value) {
                NeoPoolRewardTrackerDataStore.updateData(poolResult)

                NeoPoolRewardWidget().updateAll(context)
            }

            if (poolResult.isSuccess) {
                Result.success()
            } else {
                Result.retry()
            }
        } catch (e: Exception) {
            Result.failure()
        }
    }
}