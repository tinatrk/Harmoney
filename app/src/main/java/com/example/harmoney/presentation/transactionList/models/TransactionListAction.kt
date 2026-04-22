package com.example.harmoney.presentation.transactionList.models

sealed interface TransactionListAction {
    data class NavigateToCreatingTransaction(val categoryId: Long?) : TransactionListAction
    data class NavigateToOpeningTransaction(val transactionId: Long?) : TransactionListAction
    data object NavigateBack : TransactionListAction
}
