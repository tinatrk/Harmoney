package com.example.harmoney.presentation.transaction.models

sealed class TransactionDateError {
    data object None : TransactionDateError()
    data class OutOfRange(val firstDay: String, val lastDay: String) : TransactionDateError()
}
