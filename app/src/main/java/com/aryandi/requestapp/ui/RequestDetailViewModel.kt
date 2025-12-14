package com.aryandi.requestapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aryandi.requestapp.data.RequestService
import com.aryandi.requestapp.ui.RequestDetailEffect.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class RequestDetailEffect {
    object Idle : RequestDetailEffect()
    object Loading : RequestDetailEffect()
    object Approved : RequestDetailEffect()
    object Rejected : RequestDetailEffect()
    data class Error(val message: String) : RequestDetailEffect()
}

sealed class RequestDetailAction {
    object GetRequest : RequestDetailAction()
    object Approve : RequestDetailAction()
    object Reject : RequestDetailAction()
    object Reset : RequestDetailAction()
    // Add more actions as needed
}

data class MessageUiState(val header: String = "", val body: String = "")

@HiltViewModel
class RequestDetailViewModel @Inject constructor(
    private val requestService: RequestService
) : ViewModel() {
    private val _uiState = MutableStateFlow(MessageUiState())
    val state: StateFlow<MessageUiState> = _uiState

    private val _effect = MutableStateFlow<RequestDetailEffect>(Idle)
    val effect: StateFlow<RequestDetailEffect> = _effect

    init {
        handleAction(RequestDetailAction.GetRequest)
    }

    fun handleAction(action: RequestDetailAction) {
        when (action) {
            RequestDetailAction.GetRequest -> {
                _effect.value = Loading
                viewModelScope.launch {
                    val result = requestService.getNewRequest()
                    if (result.isSuccess) {
                        _effect.value = Idle
                        _uiState.value = MessageUiState(
                            result.getOrNull()?.heading ?: "",
                            result.getOrNull()?.content ?: ""
                        )
                    }
                }
            }

            is RequestDetailAction.Approve -> {
                _effect.value = Loading
                viewModelScope.launch {
                    val result = requestService.approveRequest()
                    _effect.value =
                        if (result.isSuccess) Approved else Error(
                            result.exceptionOrNull()?.message ?: "Unknown error"
                        )
                }
            }

            is RequestDetailAction.Reject -> {
                _effect.value = Rejected
            }

            is RequestDetailAction.Reset -> {
                _effect.value = Idle
            }

        }
    }
}
