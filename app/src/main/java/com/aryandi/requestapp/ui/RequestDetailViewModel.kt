package com.aryandi.requestapp.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aryandi.requestapp.data.RequestService
import com.aryandi.requestapp.ui.RequestDetailEffect.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class RequestDetailEffect {
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

data class RequestDetailState(
    val header: String = "",
    val body: String = "",
    val isLoading: Boolean = false
)

@HiltViewModel
class RequestDetailViewModel @Inject constructor(
    private val requestService: RequestService,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(RequestDetailState())
    val state: StateFlow<RequestDetailState> = _state

    private val _effects = MutableSharedFlow<RequestDetailEffect>()
    val effects: SharedFlow<RequestDetailEffect> = _effects.asSharedFlow()

    init {
        handleAction(RequestDetailAction.GetRequest)
    }

    fun handleAction(action: RequestDetailAction) {
        viewModelScope.launch {
            when (action) {
                RequestDetailAction.GetRequest -> {
                    _state.update { it.copy(isLoading = true) }
                    val result = requestService.getNewRequest()
                    if (result.isSuccess) {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                header = result.getOrNull()?.heading ?: "",
                                body = result.getOrNull()?.content ?: ""
                            )
                        }
                    } else {
                        _state.update { it.copy(isLoading = false) }
                        _effects.emit(RequestDetailEffect.Error("Failed to load request"))
                    }
                }

                is RequestDetailAction.Approve -> {
                    _state.update { it.copy(isLoading = true) }
                    val result = requestService.approveRequest()
                    _state.update { it.copy(isLoading = false) }
                    if (result.isSuccess) {
                        _effects.emit(Approved)
                    } else {
                        _effects.emit(
                            Error(
                                result.exceptionOrNull()?.message ?: "Unknown error"
                            )
                        )
                    }
                }

                is RequestDetailAction.Reject -> {
                    _effects.emit(Rejected)
                }

                is RequestDetailAction.Reset -> {
                    // Handle reset if needed
                }
            }
        }
    }
}
