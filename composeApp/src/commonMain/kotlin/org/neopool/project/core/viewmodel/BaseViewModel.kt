package org.neopool.project.core.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

interface UdfViewModel<UiState, UiAction, SingleEvent> {
    val uiState: StateFlow<UiState>
    val singleEvent: SharedFlow<SingleEvent>
    fun dispatch(action: UiAction)
}

@Suppress("VariableNaming", "PropertyName", "UnnecessaryAbstractClass")
abstract class BaseViewModel<UiState : Any, UiAction : Any, SingleEvent : Any>(
    initialUiState: UiState,
) : ViewModel(), UdfViewModel<UiState, UiAction, SingleEvent> {

    protected val _uiState = MutableStateFlow(initialUiState)
    override val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    protected val _singleEvent = MutableSharedFlow<SingleEvent>(extraBufferCapacity = 1)
    override val singleEvent: SharedFlow<SingleEvent> = _singleEvent.asSharedFlow()

    private val _uiActionFlow = MutableSharedFlow<UiAction>()
    val uiActionFlow: SharedFlow<UiAction> = _uiActionFlow.asSharedFlow()

    protected abstract fun reduce(uiAction: UiAction)

    final override fun dispatch(action: UiAction) {
        reduce(action)
    }
}