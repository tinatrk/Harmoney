package com.example.harmoney.presentation.converters

import com.example.harmoney.domain.models.Currency
import com.example.harmoney.presentation.models.CurrencyUi

interface CurrencyUiConverter {
    fun map(currency: Currency): CurrencyUi

    fun map(currencyList: List<Currency>): List<CurrencyUi>
}
