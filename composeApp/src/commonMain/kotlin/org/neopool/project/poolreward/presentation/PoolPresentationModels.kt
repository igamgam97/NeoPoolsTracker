package org.neopool.project.poolreward.presentation

import org.neopool.project.core.state.StateType
import org.neopool.project.core.state.StateTypeWithError

data class PoolUiState(
    val stateType: StateTypeWithError<String>,
    val hashrate1d: Long,
    val feeType: String,
    val updated: Long,
) {
    companion object {
        val Default = PoolUiState(
            stateType = StateType.loading(),
            hashrate1d = 0,
            feeType = "",
            updated = 0,
        )
    }

}

sealed interface PoolSingleEvent {
    data class ShowError(val message: String) : PoolSingleEvent
}

sealed interface PoolAction {
    data object Init : PoolAction
    data object Update : PoolAction
}