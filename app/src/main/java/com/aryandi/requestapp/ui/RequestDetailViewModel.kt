package com.aryandi.requestapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aryandi.requestapp.data.RequestService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class RequestDetailState {
    object Idle : RequestDetailState()
    object Loading : RequestDetailState()
    object Approved : RequestDetailState()
    object Rejected : RequestDetailState()
    data class Error(val message: String) : RequestDetailState()
}

sealed class RequestDetailAction {
    data class Approve(val requestId: String) : RequestDetailAction()
    object Reject : RequestDetailAction()
    object Reset : RequestDetailAction()
    // Add more actions as needed
}

@HiltViewModel
class RequestDetailViewModel @Inject constructor(
    private val requestService: RequestService
) : ViewModel() {
    private val _state = MutableStateFlow<RequestDetailState>(RequestDetailState.Idle)
    val state: StateFlow<RequestDetailState> = _state

    fun handleAction(action: RequestDetailAction) {
        when (action) {
            is RequestDetailAction.Approve -> {
                _state.value = RequestDetailState.Loading
                viewModelScope.launch {
                    val result = requestService.approveRequest(action.requestId)
                    _state.value =
                        if (result.isSuccess) RequestDetailState.Approved else RequestDetailState.Error(
                            result.exceptionOrNull()?.message ?: "Unknown error"
                        )
                }
            }

            is RequestDetailAction.Reject -> {
                _state.value = RequestDetailState.Rejected
            }

            is RequestDetailAction.Reset -> {
                _state.value = RequestDetailState.Idle
            }
        }
    }
}
