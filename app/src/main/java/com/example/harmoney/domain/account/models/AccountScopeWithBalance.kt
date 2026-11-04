package com.example.harmoney.domain.account.models

import com.example.harmoney.domain.models.AccountScope
import com.example.harmoney.domain.models.ConvertedTotal
import com.example.harmoney.domain.models.ExchangeRateStatus
import com.example.harmoney.domain.models.Money

sealed interface AccountScopeWithBalance {
    val scope: AccountScope

    data class Single(
        override val scope: AccountScope.Single,
        val balance: Money
    ) :
        AccountScopeWithBalance

    data class Group(
        override val scope: AccountScope.Group,
        val balance: ConvertedTotal,
        val exchangeRateStatuses: Set<ExchangeRateStatus>
    ) : AccountScopeWithBalance
}
