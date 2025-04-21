package org.neopool.project.widget

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.neopool.project.poolreward.data.model.PoolResponse

// TODO(GlebShcherbakov) remake to class + koin injection
object NeoPoolRewardTrackerDataStore {
    private val _poolData = MutableStateFlow<Result<PoolResponse>?>(null)
    val poolData = _poolData.asStateFlow()

    fun updateData(newData: Result<PoolResponse>) {
        _poolData.value = newData
    }
}