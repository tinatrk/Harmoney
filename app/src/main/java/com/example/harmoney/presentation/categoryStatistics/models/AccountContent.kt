package com.example.harmoney.presentation.categoryStatistics.models

import com.example.harmoney.presentation.models.AccountScopeUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class AccountContent(
    val accountScopes: ImmutableList<AccountScopeUi> = persistentListOf(),
    val accountsExchangeRateStatus: String? = null,
    val selectedAccountScope: AccountScopeUi? = null,
)
