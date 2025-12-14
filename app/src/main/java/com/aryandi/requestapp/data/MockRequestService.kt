package com.aryandi.requestapp.data

import kotlinx.coroutines.delay
import kotlin.random.Random

class MockRequestService : RequestService {
    override suspend fun approveRequest(requestId: String): Result<Unit> {
        delay(1200)
        return if (Random.nextBoolean()) Result.success(Unit) else Result.failure(Exception("Network error on approve"))
    }
}
