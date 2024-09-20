@file:OptIn(ExperimentalCoroutinesApi::class)

package com.pronoidsoftware.kotlincoroutinesandflowsmasterclass.assignment1042

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class SearchViewModelTest {

    class TestDispatchers(
        val dispatcher: CoroutineDispatcher = StandardTestDispatcher()
    ) : DispatchProvider {
        override val main: CoroutineDispatcher
            get() = dispatcher
        override val mainImmediate: CoroutineDispatcher
            get() = dispatcher
        override val io: CoroutineDispatcher
            get() = dispatcher
        override val default: CoroutineDispatcher
            get() = dispatcher
    }

    lateinit var testDispatcher: CoroutineDispatcher
    lateinit var viewModel: SearchViewModel

    @Before
    fun setUp() {
        testDispatcher = StandardTestDispatcher()
        Dispatchers.setMain(testDispatcher) // important, this must come before instantiating the viewmodel!
        viewModel = SearchViewModel(TestDispatchers(testDispatcher))
    }

    @Test
    fun `Empty query returns empty result`() = runTest(testDispatcher) {
        viewModel.state.test {
            val startState = awaitItem()
            assertThat(startState).isEqualTo(SearchState())
        }
    }

    @Test
    fun `Validation messages are accurate`() = runTest(testDispatcher) {
        viewModel.state.test {
            val startState = awaitItem()
            assertThat(startState).isEqualTo(SearchState())
            viewModel.onSearchQueryChange("9")
            val newQueryState = awaitItem()
            assertThat(newQueryState.query).isEqualTo("9")
//            val newLoadingState = awaitItem()
//            assertThat(newLoadingState.isLoading).isTrue()
            val newValidState = awaitItem()
            assertThat(newValidState.validationMessages).isEqualTo("Input cannot contain a digit")
            assertThat(newValidState.isLoading).isTrue()
        }
    }

    @Test
    fun `Expected results`() = runTest(testDispatcher) {
        viewModel.state.test {
            val startState = awaitItem()
            assertThat(startState).isEqualTo(SearchState())
            viewModel.onSearchQueryChange("cup")
            val newQueryState = awaitItem()
            assertThat(newQueryState.query).isEqualTo("cup")
            awaitItem()
            val result = awaitItem()
            assertThat(result.searchResults).isEqualTo(listOf("Buttercup", "Cupcake"))
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

}