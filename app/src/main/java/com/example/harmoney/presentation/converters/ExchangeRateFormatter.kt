package com.example.harmoney.presentation.converters

import com.example.harmoney.domain.models.ExchangeRateStatus

interface ExchangeRateFormatter {
    fun map(rates: Set<ExchangeRateStatus>): String?
}
