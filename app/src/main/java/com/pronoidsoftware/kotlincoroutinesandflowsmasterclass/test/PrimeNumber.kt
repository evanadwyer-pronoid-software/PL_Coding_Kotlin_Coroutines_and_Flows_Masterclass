package com.pronoidsoftware.kotlincoroutinesandflowsmasterclass.test

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * This is the function that you need to write unit tests for
 */
suspend fun findPrimeNumberAtIndex(
    index: Int,
    dispatcher: CoroutineDispatcher
    ): Int {
    return withContext(dispatcher) {
        require(index > 0) { "Index should be greater than 0" }
        var primeCounter = 0
        var numberToCheck = 2
        while (primeCounter < index) {
            if (isPrimeNumber(numberToCheck)) {
                primeCounter++
            }
            if (primeCounter < index) {
                numberToCheck++
            }
        }
        numberToCheck
    }
}
fun isPrimeNumber(num: Int): Boolean {
    if (num <= 1) return false
    for (i in 2..num / 2) {
        if (num % i == 0) {
            return false
        }
    }
    return true
}