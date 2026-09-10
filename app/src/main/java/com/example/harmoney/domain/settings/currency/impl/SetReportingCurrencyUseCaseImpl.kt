package com.example.harmoney.domain.settings.currency.impl

import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.settings.currency.api.repository.ReportingCurrencyRepository
import com.example.harmoney.domain.settings.currency.api.useCase.SetReportingCurrencyUseCase

class SetReportingCurrencyUseCaseImpl(private val repository: ReportingCurrencyRepository) :
    SetReportingCurrencyUseCase {
    override suspend fun execute(currency: Currency) {
        repository.setReportingCurrency(currency = currency)
    }
}
