package com.example.harmoney.presentation.converters

import com.example.harmoney.domain.models.AccountScope
import com.example.harmoney.domain.models.ConvertedTotal
import com.example.harmoney.domain.models.Money
import com.example.harmoney.presentation.models.AccountScopeUi

interface AccountScopeUiConverter {
    fun singleMap(scope: AccountScope.Single, balance: Money): AccountScopeUi

    fun groupMap(scope: AccountScope.Group, balance: ConvertedTotal): AccountScopeUi
}
