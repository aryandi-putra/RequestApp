package com.aryandi.requestapp.data

interface RequestService {
    suspend fun getNewRequest(): Result<Request>
    suspend fun approveRequest(): Result<Unit>
}
