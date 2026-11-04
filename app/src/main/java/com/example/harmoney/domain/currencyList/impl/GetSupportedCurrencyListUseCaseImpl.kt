package com.example.harmoney.domain.currencyList.impl

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.currencyList.api.repository.CurrencyListRepository
import com.example.harmoney.domain.currencyList.api.useCase.GetSupportedCurrencyListUseCase
import com.example.harmoney.domain.currencyList.models.CurrencyListFailure
import com.example.harmoney.domain.models.Currency

class GetSupportedCurrencyListUseCaseImpl(
    private val repository: CurrencyListRepository
) : GetSupportedCurrencyListUseCase {
    override suspend fun execute(isRefreshRequired: Boolean)
            : Resource<List<Currency>, CurrencyListFailure> {
        return if (isRefreshRequired) {
            repository.getRefreshedSupportedCurrencyList()
        } else {
            repository.getSupportedCurrencyList()
        }
    }
}
