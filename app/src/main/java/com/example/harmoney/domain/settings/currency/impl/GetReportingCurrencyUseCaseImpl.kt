package com.example.harmoney.domain.settings.currency.impl

import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.settings.currency.api.repository.ReportingCurrencyRepository
import com.example.harmoney.domain.settings.currency.api.useCase.GetReportingCurrencyUseCase
import kotlinx.coroutines.flow.Flow

class GetReportingCurrencyUseCaseImpl(private val repository: ReportingCurrencyRepository) :
    GetReportingCurrencyUseCase {
    override fun invoke(): Flow<Currency> {
        return repository.getReportingCurrency()
    }
}
