package org.neopool.project.widget

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.neopool.project.core.state.StateType

class NewPoolRewardWidgetViewModel {

    private val _state = MutableStateFlow(PoolWidgetUiState.Default)
    val state = _state.asStateFlow()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + Job())

    fun init() {
        coroutineScope.launch {
            _state.update {
                it.copy(
                    stateType = StateType.loading(),
                )
            }
            subscribeToNetworkEvents()
        }
    }

    private suspend fun subscribeToNetworkEvents() {
        NeoPoolRewardTrackerDataStore.poolData.collect { poolResponse ->
            poolResponse?.fold(
                onSuccess = { result ->
                    _state.update {
                        PoolWidgetUiState(
                            stateType = StateType.data(),
                            hashrate1d = result.data.hashrate1d,
                            feeType = result.data.feeType,
                            updated = result.updated,
                        )
                    }
                },
                onFailure = { error ->
                    _state.update { state ->
                        state.copy(
                            stateType = StateType.error(error.message ?: "Unknown error"),
                        )
                    }
                },
            )
        }
    }

    fun retry() {
        _state.update {
            it.copy(
                stateType = StateType.loading(),
            )
        }
    }

    fun clean() {
        coroutineScope.cancel()
    }
}