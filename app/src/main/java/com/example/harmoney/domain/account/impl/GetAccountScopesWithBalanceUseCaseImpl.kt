package com.example.harmoney.domain.account.impl

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.account.api.useCase.GetAccountScopesWithBalanceUseCase
import com.example.harmoney.domain.account.models.AccountScopeWithBalance
import com.example.harmoney.domain.exchangeRate.api.useCase.ConvertTotalMoneyUseCase
import com.example.harmoney.domain.exchangeRate.models.ExchangeRateFailure
import com.example.harmoney.domain.models.AccountScope
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.models.Money

class GetAccountScopesWithBalanceUseCaseImpl(
    private val convertTotalMoneyUseCase: ConvertTotalMoneyUseCase,
) : GetAccountScopesWithBalanceUseCase {
    override suspend fun execute(
        scopes: List<AccountScope>,
        targetCurrency: Currency
    ): Resource<List<AccountScopeWithBalance>, ExchangeRateFailure> {
        val result = mutableListOf<AccountScopeWithBalance>()

        for (scope in scopes) {
            when (scope) {
                is AccountScope.Single -> result += AccountScopeWithBalance.Single(
                    scope = scope,
                    balance = Money(
                        amount = scope.account.currentBalance,
                        currency = scope.account.currency
                    )
                )

                is AccountScope.Group -> {
                    val balances = scope.accounts.map { account ->
                        Money(amount = account.currentBalance, currency = account.currency)
                    }

                    when (val converted = convertTotalMoneyUseCase.execute(
                        moneyList = balances,
                        targetCurrency = targetCurrency
                    )) {
                        is Resource.Error -> return Resource.Error(converted.error)

                        is Resource.Success -> result += AccountScopeWithBalance.Group(
                            scope = scope,
                            balance = converted.data.total,
                            exchangeRateStatuses = converted.data.exchangeRates
                        )
                    }
                }
            }
        }
        return Resource.Success(result)
    }
}
