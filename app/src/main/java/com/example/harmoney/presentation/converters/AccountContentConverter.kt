package com.example.harmoney.presentation.converters

import com.example.harmoney.domain.account.models.AccountScopeWithBalance
import com.example.harmoney.domain.models.AccountScope
import com.example.harmoney.presentation.categoryStatistics.models.AccountContent

interface AccountContentConverter {
    fun map(
        scopes: List<AccountScopeWithBalance>,
        selectedScope: AccountScope
    ): AccountContent
}
