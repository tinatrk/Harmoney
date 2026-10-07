package com.example.harmoney.domain.settings.currency.impl

import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.settings.currency.api.repository.ReportingCurrencyRepository
import com.example.harmoney.domain.settings.currency.api.useCase.ReportingCurrencyInteractor
import kotlinx.coroutines.flow.Flow

class ReportingCurrencyInteractorImpl(
    private val repository: ReportingCurrencyRepository
) : ReportingCurrencyInteractor {
    override fun getReportingCurrency(): Flow<Currency> {
        return repository.getReportingCurrency()
    }

    override suspend fun saveReportingCurrency(currency: Currency) {
        repository.setReportingCurrency(currency)
    }
}
