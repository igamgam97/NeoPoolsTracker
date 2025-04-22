package org.neopool.project.di

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.neopool.project.poolreward.data.model.PoolResponse
import org.neopool.project.poolreward.data.repository.PoolRepository

/**
 *  Class for providing dependencies to ios.
 */
object KoinInitializer {
    private var isInitialized = false
    fun startKoin() {
        if (isInitialized) return
        startKoin {
            modules(appModule())
        }
        isInitialized = true
    }
}

sealed class PoolResult {
    data class Success(val data: PoolResponse) : PoolResult()
    data class Failure(val message: String) : PoolResult()
}

class KoinHelper : KoinComponent {
    private val poolRewardRepository: PoolRepository by inject()

    suspend fun getReward(): PoolResult {
        val result = poolRewardRepository.getPoolData()
        return if (result.isSuccess) {
            PoolResult.Success(result.getOrThrow())
        } else {
            PoolResult.Failure(
                result.exceptionOrNull()?.message ?: "Unknown error",
            )
        }
    }
}