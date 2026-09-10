package com.example.harmoney.presentation.currency.models

import androidx.compose.runtime.Stable
import com.example.harmoney.presentation.models.CurrencyUi

@Stable
sealed class CurrencyEvent {
    data object OnBackClick : CurrencyEvent()

    data object OnClearSearchLineClick : CurrencyEvent()
    data class OnSearchTextChanged(val newText: String) : CurrencyEvent()
    data object OnSearchStartClick : CurrencyEvent()

    data class OnCurrencyClick(val newCurrency: CurrencyUi) : CurrencyEvent()
}
