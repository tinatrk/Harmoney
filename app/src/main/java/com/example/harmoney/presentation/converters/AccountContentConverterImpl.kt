package com.example.harmoney.presentation.converters

import com.example.harmoney.domain.account.models.AccountScopeWithBalance
import com.example.harmoney.domain.models.AccountScope
import com.example.harmoney.presentation.categoryStatistics.models.AccountContent
import com.example.harmoney.presentation.models.AccountScopeUi
import kotlinx.collections.immutable.toImmutableList

class AccountContentConverterImpl(
    private val accountScopeUiConverter: AccountScopeUiConverter,
    private val exchangeRateFormatter: ExchangeRateFormatter,
) : AccountContentConverter {
    override fun map(
        scopes: List<AccountScopeWithBalance>,
        selectedScope: AccountScope
    ): AccountContent {
        val scopesUi = scopes.map { it.toUi() }.toImmutableList()

        val selectedScope = scopesUi.firstOrNull { it.id == selectedScope.id }

        val exchangeRateStatuses = scopes
            .filterIsInstance<AccountScopeWithBalance.Group>()
            .flatMap { it.exchangeRateStatuses }
            .toSet()

        return AccountContent(
            accountScopes = scopesUi,
            selectedAccountScope = selectedScope,
            accountsExchangeRateStatus =
                exchangeRateFormatter.map(exchangeRateStatuses)
        )
    }

    private fun AccountScopeWithBalance.toUi(): AccountScopeUi =
        when (this) {
            is AccountScopeWithBalance.Single ->
                accountScopeUiConverter.singleMap(scope, balance)

            is AccountScopeWithBalance.Group ->
                accountScopeUiConverter.groupMap(scope, balance)
        }
}
