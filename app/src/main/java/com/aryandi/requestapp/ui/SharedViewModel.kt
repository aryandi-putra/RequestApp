package com.aryandi.requestapp.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class RequestResultEvent {
    object Approved : RequestResultEvent()
    object Rejected : RequestResultEvent()
    data class Error(val message: String) : RequestResultEvent()
}

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {
    private val _requestResultEvents =
        MutableSharedFlow<RequestResultEvent>(extraBufferCapacity = 1)
    val requestResultEvents: SharedFlow<RequestResultEvent> = _requestResultEvents.asSharedFlow()

    fun emitEvent(event: RequestResultEvent) {
        CoroutineScope(Dispatchers.Main).launch {
            _requestResultEvents.emit(event)
        }
    }
}
