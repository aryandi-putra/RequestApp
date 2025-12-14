package com.aryandi.requestapp.data

import kotlinx.coroutines.delay
import kotlin.random.Random

class MockRequestService : RequestService {
    override suspend fun approveRequest(): Result<Unit> {
        delay(1200)
        return if (Random.nextBoolean()) Result.success(Unit) else Result.failure(Exception("Network error on approve"))
    }

    override suspend fun getNewRequest(): Result<Request> {
        return Result.success(
            Request(
                "Heading 1",
                "Lorem ipsum dolor sit amet consectetur. Arcu tincidunt vitae cras amet. Blandit id sed et est gravida. Eu sapien amet et volutpat ultrices sed. Euismod semper mi non vitae egestas sollicitudin aliquam."
            )
        )
    }
}
