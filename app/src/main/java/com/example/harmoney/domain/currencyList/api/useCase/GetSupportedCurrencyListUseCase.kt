package com.example.harmoney.domain.currencyList.api.useCase

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.currencyList.models.CurrencyListFailure
import com.example.harmoney.domain.models.Currency

interface GetSupportedCurrencyListUseCase {
    suspend fun execute(isRefreshRequired: Boolean = false)
            : Resource<List<Currency>, CurrencyListFailure>
}
