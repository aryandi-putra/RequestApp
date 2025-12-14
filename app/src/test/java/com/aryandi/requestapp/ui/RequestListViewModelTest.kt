package com.aryandi.requestapp.ui

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RequestListViewModelTest {
    private lateinit var viewModel: RequestListViewModel

    @Before
    fun setup() {
        viewModel = RequestListViewModel()
    }

    @Test
    fun `initial state is Idle`() = runTest {
        assertEquals(RequestListState.Idle, viewModel.state.value)
    }

    @Test
    fun `handle OnCreateRequestClick updates state to CreatedRequest`() = runTest {
        viewModel.handleAction(RequestListAction.OnCreateRequestClick)
        assertEquals(RequestListState.CreatedRequest, viewModel.state.value)
    }

    @Test
    fun `handle OnApproved updates state to ShowGreenSnackbar`() = runTest {
        viewModel.handleAction(RequestListAction.OnApproved)
        assertEquals(RequestListState.ShowGreenSnackbar, viewModel.state.value)
    }

    @Test
    fun `handle OnRejected updates state to ShowRedSnackbar`() = runTest {
        viewModel.handleAction(RequestListAction.OnRejected)
        assertEquals(RequestListState.ShowRedSnackbar, viewModel.state.value)
    }

    @Test
    fun `handle Reset updates state to Idle`() = runTest {
        viewModel.handleAction(RequestListAction.OnCreateRequestClick)
        viewModel.handleAction(RequestListAction.Reset)
        assertEquals(RequestListState.Idle, viewModel.state.value)
    }

    @Test
    fun `multiple actions sequence updates state correctly`() = runTest {
        viewModel.handleAction(RequestListAction.OnCreateRequestClick)
        assertEquals(RequestListState.CreatedRequest, viewModel.state.value)
        viewModel.handleAction(RequestListAction.OnApproved)
        assertEquals(RequestListState.ShowGreenSnackbar, viewModel.state.value)
        viewModel.handleAction(RequestListAction.OnRejected)
        assertEquals(RequestListState.ShowRedSnackbar, viewModel.state.value)
        viewModel.handleAction(RequestListAction.Reset)
        assertEquals(RequestListState.Idle, viewModel.state.value)
    }

    @Test
    fun `repeated actions do not cause unexpected state changes`() = runTest {
        viewModel.handleAction(RequestListAction.OnApproved)
        assertEquals(RequestListState.ShowGreenSnackbar, viewModel.state.value)
        viewModel.handleAction(RequestListAction.OnApproved)
        assertEquals(RequestListState.ShowGreenSnackbar, viewModel.state.value)
        viewModel.handleAction(RequestListAction.OnRejected)
        assertEquals(RequestListState.ShowRedSnackbar, viewModel.state.value)
        viewModel.handleAction(RequestListAction.OnRejected)
        assertEquals(RequestListState.ShowRedSnackbar, viewModel.state.value)
    }

    @Test
    fun `reset always returns state to Idle from any state`() = runTest {
        val actions = listOf(
            RequestListAction.OnCreateRequestClick,
            RequestListAction.OnApproved,
            RequestListAction.OnRejected
        )
        for (action in actions) {
            viewModel.handleAction(action)
            viewModel.handleAction(RequestListAction.Reset)
            assertEquals(RequestListState.Idle, viewModel.state.value)
        }
    }
}
