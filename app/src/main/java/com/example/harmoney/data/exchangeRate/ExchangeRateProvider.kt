package com.example.harmoney.data.exchangeRate

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.exchangeRate.models.ExchangeRateFailure
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.models.ExchangeRate

interface ExchangeRateProvider {
    suspend fun getAvailableRatesFromBaseToQuote(
        baseCurrency: Currency,
        quoteCurrencies: Set<Currency>,
    ): Resource<List<ExchangeRate>, ExchangeRateFailure>
}
