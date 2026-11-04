package com.example.harmoney.domain.converter

import com.example.harmoney.domain.models.ExchangeRateResult
import com.example.harmoney.domain.models.ExchangeRateStatus

interface ExchangeRateResultConverter {
    fun map(result: ExchangeRateResult): ExchangeRateStatus

    fun map(results: Set<ExchangeRateResult>): Set<ExchangeRateStatus>
}
