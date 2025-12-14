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

@HiltViewModel
class RequestDetailViewModel @Inject constructor(
    private val requestService: RequestService
) : ViewModel() {
    private val _state = MutableStateFlow<RequestDetailState>(RequestDetailState.Idle)
    val state: StateFlow<RequestDetailState> = _state

    fun approve(requestId: String) {
        _state.value = RequestDetailState.Loading
        viewModelScope.launch {
            val result = requestService.approveRequest(requestId)
            _state.value =
                if (result.isSuccess) RequestDetailState.Approved else RequestDetailState.Error(
                    result.exceptionOrNull()?.message ?: "Unknown error"
                )
        }
    }

    fun reject() {
        _state.value = RequestDetailState.Rejected
    }

    fun reset() {
        _state.value = RequestDetailState.Idle
    }
}
