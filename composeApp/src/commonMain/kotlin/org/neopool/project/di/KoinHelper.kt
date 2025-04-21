package org.neopool.project.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
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
    private val poolReward: PoolRepository by inject()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    fun greet(callback: (PoolResult) -> Unit) {
        coroutineScope.launch {
            val result = poolReward.getPoolData()
            when {
                result.isSuccess -> callback(PoolResult.Success(result.getOrNull()!!))
                result.isFailure -> callback(PoolResult.Failure(result.exceptionOrNull()?.message ?: "Unknown error"))
            }
        }
    }
}