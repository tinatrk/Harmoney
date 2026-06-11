package com.example.harmoney.presentation.transaction.models

sealed interface TransactionAction {
    data object NavigateBack : TransactionAction
    data object NavigateToCategoryScreen : TransactionAction
}
