package com.example.harmoney.presentation.transaction.models

sealed interface TransactionAction {
    data object NavigateBack : TransactionAction
    data class NavigateToCategoryListScreen(val categoryTypeId: Long?) : TransactionAction
}
