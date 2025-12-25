package com.aryandi.requestapp.ui

import kotlinx.serialization.Serializable
import androidx.navigation3.runtime.NavKey

@Serializable
sealed class Route : NavKey {
    @Serializable
    data class RequestList(val result: RequestResult? = null) : Route()

    @Serializable
    data class RequestDetail(val requestId: String) : Route()
}

@Serializable
enum class RequestResult {
    APPROVED, REJECTED
}
