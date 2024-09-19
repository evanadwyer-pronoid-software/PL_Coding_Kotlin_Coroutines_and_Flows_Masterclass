package com.pronoidsoftware.kotlincoroutinesandflowsmasterclass.test

import assertk.assertions.isEqualTo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class PrimeNumberTest {

    lateinit var dispatcher: CoroutineDispatcher

    @Before
    fun setUp() {
        dispatcher = StandardTestDispatcher()
    }

    @Test
    fun testIndexedPrimeNumber() = runTest(dispatcher) {
        val result = findPrimeNumberAtIndex(1000, dispatcher)
        assertk.assertThat(result).isEqualTo(7919)
    }
}