package com.pronoidsoftware.kotlincoroutinesandflowsmasterclass.charity

sealed interface ProcessingState {
    data object CheckingFunds : ProcessingState
    data object DebitingAccount : ProcessingState
    data object CreditingAccount : ProcessingState
    data object CleanupResources : ProcessingState
}