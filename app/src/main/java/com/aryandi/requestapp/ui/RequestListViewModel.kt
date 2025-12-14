package com.aryandi.requestapp.ui

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@Stable
sealed class RequestListAction {
    object Refresh : RequestListAction()
    object CreateRequest : RequestListAction()
    data class ShowSnackbar(val message: String) : RequestListAction()
    // Extend with other actions as needed
}

@HiltViewModel
class RequestListViewModel @Inject constructor() : ViewModel() {

    fun handleAction(action: RequestListAction) {
        when (action) {
            is RequestListAction.Refresh -> {
                // TODO: implement refresh logic
            }

            is RequestListAction.CreateRequest -> {
                // TODO: implement create-request logic (navigation/UI)
            }

            is RequestListAction.ShowSnackbar -> {
                // TODO: trigger snackbar with message action.message
            }
            // Extend handling as needed
        }
    }
}