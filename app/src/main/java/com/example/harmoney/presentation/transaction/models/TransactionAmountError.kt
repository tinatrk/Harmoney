package com.example.harmoney.presentation.transaction.models

sealed class TransactionAmountError {
    data object None : TransactionAmountError()
    data object IncorrectInput : TransactionAmountError()
    data object Empty: TransactionAmountError()
}
