package org.neopool.project.poolreward.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.neopool.project.core.state.StateType
import org.neopool.project.core.viewmodel.BaseViewModel
import org.neopool.project.poolreward.data.repository.PoolRepository

class PoolViewModel(
    private val repository: PoolRepository,
) : BaseViewModel<PoolUiState, PoolAction, PoolSingleEvent>(
    initialUiState = PoolUiState.Default,
) {

    override fun reduce(uiAction: PoolAction) {
        when (uiAction) {
            is PoolAction.Init -> {
                loadPoolData()
            }

            is PoolAction.Update -> {
                loadPoolData()
            }
        }
    }

    private fun loadPoolData() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    stateType = StateType.loading(),
                )
            }
            repository.getPoolData()
                .fold(
                    onSuccess = { response ->
                        _uiState.update {
                            it.copy(
                                stateType = StateType.data(),
                                hashrate1d = response.data.hashrate1d,
                                feeType = response.data.feeType,
                                updated = response.updated,
                            )
                        }
                    },
                    onFailure = { error ->
                        val errorMessage = error.message ?: "Unknown error occurred"
                        _uiState.update {
                            it.copy(
                                stateType = StateType.error(
                                    errorMessage,
                                ),
                            )
                        }
                        _singleEvent.emit(
                            PoolSingleEvent.ShowError(
                                errorMessage,
                            ),
                        )
                    },
                )
        }
    }
}