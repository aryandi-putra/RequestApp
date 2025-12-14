package com.aryandi.requestapp.ui

import com.aryandi.requestapp.data.Request
import com.aryandi.requestapp.data.RequestService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class FakeRequestService : RequestService {
    var shouldSucceed = true
    override suspend fun getNewRequest(): Result<Request> {
        return if (shouldSucceed) Result.success(
            Request(
                "REQ_42",
                "Sample Request"
            )
        ) else Result.failure(Exception("request failed"))
    }

    override suspend fun approveRequest(): Result<Unit> {
        return if (shouldSucceed) Result.success(Unit) else Result.failure(Exception("Simulated failure"))
    }
}


@OptIn(ExperimentalCoroutinesApi::class)
class RequestDetailViewModelTest {

    private lateinit var fakeService: FakeRequestService
    private lateinit var viewModel: RequestDetailViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeService = FakeRequestService()
        viewModel = RequestDetailViewModel(fakeService)
    }

    @Test
    fun `successful approval triggers Approved state`() = runTest {
        fakeService.shouldSucceed = true
        viewModel.handleAction(RequestDetailAction.Approve)
        advanceUntilIdle()
        val state = viewModel.effect.value
        assertTrue(state is RequestDetailEffect.Approved)
    }

    @Test
    fun `failed approval triggers Error state`() = runTest {
        fakeService.shouldSucceed = false
        viewModel.handleAction(RequestDetailAction.Approve)
        advanceUntilIdle()
        val state = viewModel.effect.value
        assertTrue(state is RequestDetailEffect.Error)
        assertTrue((state as RequestDetailEffect.Error).message.contains("Simulated failure"))
    }

    @Test
    fun `rejection triggers Rejected state`() = runTest {
        viewModel.handleAction(RequestDetailAction.Reject)
        val state = viewModel.effect.value
        assertTrue(state is RequestDetailEffect.Rejected)
    }
}
