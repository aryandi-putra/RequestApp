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
    fun `initial state is Idle and MessageUiState is empty`() = runTest {
        advanceUntilIdle()
        assertEquals(RequestDetailEffect.Idle, viewModel.effect.value)
        val uiState = viewModel.state.value
        assertEquals("REQ_42", uiState.header)
        assertEquals("Sample Request", uiState.body)
    }

    @Test
    fun `Reset sets effect to Idle from any state`() = runTest {
        viewModel.handleAction(RequestDetailAction.Approve)
        advanceUntilIdle()
        assertNotEquals(RequestDetailEffect.Idle, viewModel.effect.value)
        viewModel.handleAction(RequestDetailAction.Reset)
        assertEquals(RequestDetailEffect.Idle, viewModel.effect.value)

        viewModel.handleAction(RequestDetailAction.Reject)
        assertEquals(RequestDetailEffect.Rejected, viewModel.effect.value)
        viewModel.handleAction(RequestDetailAction.Reset)
        assertEquals(RequestDetailEffect.Idle, viewModel.effect.value)
    }

    @Test
    fun `Approve then Reset sequence sets Idle`() = runTest {
        fakeService.shouldSucceed = true
        viewModel.handleAction(RequestDetailAction.Approve)
        advanceUntilIdle()
        assertEquals(RequestDetailEffect.Approved, viewModel.effect.value)
        viewModel.handleAction(RequestDetailAction.Reset)
        assertEquals(RequestDetailEffect.Idle, viewModel.effect.value)
    }

    @Test
    fun `Reject then Reset sequence sets Idle`() = runTest {
        viewModel.handleAction(RequestDetailAction.Reject)
        assertEquals(RequestDetailEffect.Rejected, viewModel.effect.value)
        viewModel.handleAction(RequestDetailAction.Reset)
        assertEquals(RequestDetailEffect.Idle, viewModel.effect.value)
    }

    @Test
    fun `GetRequest updates MessageUiState on success`() = runTest {
        viewModel.handleAction(RequestDetailAction.GetRequest)
        advanceUntilIdle()
        assertEquals("REQ_42", viewModel.state.value.header)
        assertEquals("Sample Request", viewModel.state.value.body)
    }

    @Test
    fun `multiple sequential actions result in correct effect`() = runTest {
        // Approve success
        fakeService.shouldSucceed = true
        viewModel.handleAction(RequestDetailAction.Approve)
        advanceUntilIdle()
        assertEquals(RequestDetailEffect.Approved, viewModel.effect.value)

        // Reject
        viewModel.handleAction(RequestDetailAction.Reject)
        assertEquals(RequestDetailEffect.Rejected, viewModel.effect.value)

        // Reset
        viewModel.handleAction(RequestDetailAction.Reset)
        assertEquals(RequestDetailEffect.Idle, viewModel.effect.value)
    }

    @Test
    fun `multiple GetRequest calls are idempotent`() = runTest {
        viewModel.handleAction(RequestDetailAction.GetRequest)
        advanceUntilIdle()
        val first = viewModel.state.value
        viewModel.handleAction(RequestDetailAction.GetRequest)
        advanceUntilIdle()
        val second = viewModel.state.value
        assertEquals(first, second)
    }

    @Test
    fun `error on approve does not update MessageUiState`() = runTest {
        fakeService.shouldSucceed = false
        viewModel.handleAction(RequestDetailAction.Approve)
        advanceUntilIdle()
        val state = viewModel.state.value
        // state does not change from last successful get request
        assertEquals("REQ_42", state.header)
        assertEquals("Sample Request", state.body)
    }

    // retaining previously existing tests for completeness
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
