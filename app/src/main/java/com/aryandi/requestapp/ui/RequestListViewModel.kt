package com.aryandi.requestapp.ui

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@Stable
sealed class RequestListAction {
    object Reset : RequestListAction()
    object OnCreateRequestClick : RequestListAction()
    object OnApproved : RequestListAction()
    object OnRejected : RequestListAction()
    // Extend with other actions as needed
}

sealed class RequestListState {
    object Idle : RequestListState()
    object CreatedRequest : RequestListState()
    object ShowGreenSnackbar : RequestListState()
    object ShowRedSnackbar : RequestListState()
}

@HiltViewModel
class RequestListViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow<RequestListState>(RequestListState.Idle)
    val state: StateFlow<RequestListState> = _state

    fun handleAction(action: RequestListAction) {
        when (action) {
            is RequestListAction.OnCreateRequestClick -> {
                _state.value = RequestListState.CreatedRequest
            }

            RequestListAction.OnApproved -> {
                _state.value = RequestListState.ShowGreenSnackbar
            }
            RequestListAction.OnRejected -> {
                _state.value = RequestListState.ShowRedSnackbar
            }

            RequestListAction.Reset -> {
                _state.value = RequestListState.Idle
            }
        }
    }
}