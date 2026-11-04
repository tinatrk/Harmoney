package com.example.harmoney.domain.exchangeRate.impl

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.exchangeRate.api.repository.ExchangeRateRepository
import com.example.harmoney.domain.exchangeRate.api.useCase.GetExchangeRatesUseCase
import com.example.harmoney.domain.exchangeRate.models.ExchangeRateFailure
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.models.ExchangeRateResult

class GetExchangeRatesUseCaseImpl(private val repository: ExchangeRateRepository) :
    GetExchangeRatesUseCase {
    override suspend fun execute(
        sources: Set<Currency>,
        target: Currency,
    ): Resource<List<ExchangeRateResult>, ExchangeRateFailure> {
        return repository.getRates(sources, target)
    }
}
