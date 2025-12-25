package com.aryandi.requestapp.ui

import android.util.Log
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@Stable
sealed class RequestListAction {
    object Reset : RequestListAction()
    object OnCreateRequestClick : RequestListAction()
    object OnApproved : RequestListAction()
    object OnRejected : RequestListAction()
    // Extend with other actions as needed
}

sealed class RequestListEvent {
    object CreatedRequest : RequestListEvent()
    object ShowGreenSnackbar : RequestListEvent()
    object ShowRedSnackbar : RequestListEvent()
}

@HiltViewModel
class RequestListViewModel @Inject constructor() : ViewModel() {

    private val _events = MutableSharedFlow<RequestListEvent>()
    val events: SharedFlow<RequestListEvent> = _events.asSharedFlow()

    fun handleAction(action: RequestListAction) {
        viewModelScope.launch {
            when (action) {
                is RequestListAction.OnCreateRequestClick -> {
                    _events.emit(RequestListEvent.CreatedRequest)
                }

                RequestListAction.OnApproved -> {
                    _events.emit(RequestListEvent.ShowGreenSnackbar)
                }

                RequestListAction.OnRejected -> {
                    _events.emit(RequestListEvent.ShowRedSnackbar)
                }

                RequestListAction.Reset -> {
                    // No-op for events, or handle if needed
                }
            }
        }
    }
}