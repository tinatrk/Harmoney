package com.example.harmoney.presentation.currency.models

sealed interface CurrencyAction {
    data object NavigateBack : CurrencyAction
}
