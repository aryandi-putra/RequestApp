package com.aryandi.requestapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

sealed class RequestResult {
    data class Success(val message: String) : RequestResult()
    data class Error(val error: String) : RequestResult()
}

class RequestListViewModel : ViewModel() {
    private val _resultFlow = MutableSharedFlow<RequestResult>()
    val resultFlow = _resultFlow.asSharedFlow()

    fun simulateRequest(success: Boolean) {
        viewModelScope.launch {
            if (success) {
                _resultFlow.emit(RequestResult.Success("Request completed!"))
            } else {
                _resultFlow.emit(RequestResult.Error("Request failed!"))
            }
        }
    }
}
