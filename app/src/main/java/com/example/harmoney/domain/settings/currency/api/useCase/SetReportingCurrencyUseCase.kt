package com.example.harmoney.domain.settings.currency.api.useCase

import com.example.harmoney.domain.models.Currency

interface SetReportingCurrencyUseCase {
    suspend fun execute(currency: Currency)
}
