package com.pronoidsoftware.kotlincoroutinesandflowsmasterclass.charity

import androidx.compose.foundation.text.input.TextFieldState

data class MoneyTransferState(
    val savingsBalance: Double = 1000.0,
    val checkingBalance: Double = 500.0,
    val transferAmount: TextFieldState = TextFieldState(),
    val isTransferring: Boolean = false,
    val resultMessage: String? = null,
    val processingState: ProcessingState? = null,
)