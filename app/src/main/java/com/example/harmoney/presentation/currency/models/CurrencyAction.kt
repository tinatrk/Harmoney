package com.example.harmoney.presentation.currency.models

import com.example.harmoney.domain.models.Currency

sealed interface CurrencyAction {
    data object NavigateBack : CurrencyAction

    data class SetCurrencyError(val newCurrency: Currency) : CurrencyAction
}
