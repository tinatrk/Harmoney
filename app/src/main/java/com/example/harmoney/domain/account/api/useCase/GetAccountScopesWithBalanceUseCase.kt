package com.example.harmoney.domain.account.api.useCase

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.exchangeRate.models.ExchangeRateFailure
import com.example.harmoney.domain.models.AccountScope
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.account.models.AccountScopeWithBalance

interface GetAccountScopesWithBalanceUseCase {
    suspend fun execute(
        scopes: List<AccountScope>,
        targetCurrency: Currency,
    ): Resource<List<AccountScopeWithBalance>, ExchangeRateFailure>
}
