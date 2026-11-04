package com.example.harmoney.domain.settings.currency.api.repository

import com.example.harmoney.domain.models.Currency
import kotlinx.coroutines.flow.Flow

interface ReportingCurrencyRepository {
    fun getReportingCurrency(): Flow<Currency>

    suspend fun setReportingCurrency(currency: Currency)
}
