package com.pronoidsoftware.kotlincoroutinesandflowsmasterclass.charity

sealed interface MoneyTransferAction {
    data object TransferFunds : MoneyTransferAction
    data object CancelTransfer : MoneyTransferAction
}