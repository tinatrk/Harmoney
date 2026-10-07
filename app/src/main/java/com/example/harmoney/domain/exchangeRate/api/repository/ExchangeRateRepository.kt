package com.example.harmoney.domain.exchangeRate.api.repository

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.exchangeRate.models.ExchangeRateFailure
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.models.ExchangeRateResult

interface ExchangeRateRepository {
    suspend fun getRates(
        sources: Set<Currency>,
        target: Currency,
    ): Resource<List<ExchangeRateResult>, ExchangeRateFailure>
}
