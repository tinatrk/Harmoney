package com.example.harmoney.domain.exchangeRate.api.repository

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.exchangeRate.models.ExchangeRateFailure
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.models.ExchangeRate
import java.time.LocalDate

interface ExchangeRateRepository {

    suspend fun getRates(
        baseCurrency: Currency,
        quoteCurrencies: Set<Currency>,
    ): Resource<List<ExchangeRate>, ExchangeRateFailure>
}
