package com.example.harmoney.domain.settings.currency.api.useCase

import com.example.harmoney.domain.models.Currency
import kotlinx.coroutines.flow.Flow

interface ReportingCurrencyInteractor {
    fun getReportingCurrency(): Flow<Currency>

    suspend fun saveReportingCurrency(currency: Currency)
}
