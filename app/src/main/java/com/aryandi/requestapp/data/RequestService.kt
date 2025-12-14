package com.aryandi.requestapp.data

interface RequestService {
    suspend fun approveRequest(requestId: String): Result<Unit>
}
