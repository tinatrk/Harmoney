package com.example.harmoney.domain.account.api.useCase

import com.example.harmoney.domain.models.Account
import com.example.harmoney.domain.models.AccountScope

interface GetAccountScopesUseCase {
    fun execute(accounts: List<Account>): List<AccountScope>
}
