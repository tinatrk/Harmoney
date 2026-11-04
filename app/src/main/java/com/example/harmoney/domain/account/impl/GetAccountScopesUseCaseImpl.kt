package com.example.harmoney.domain.account.impl

import com.example.harmoney.domain.account.api.useCase.GetAccountScopesUseCase
import com.example.harmoney.domain.models.Account
import com.example.harmoney.domain.models.AccountGroupType
import com.example.harmoney.domain.models.AccountScope

class GetAccountScopesUseCaseImpl : GetAccountScopesUseCase {
    override fun execute(accounts: List<Account>): List<AccountScope> {
        val notArchived = AccountScope.Group(
            type = AccountGroupType.NOT_ARCHIVED,
            accounts = accounts.filterNot { it.isArchived })
        val singleAccounts = accounts.map { account -> AccountScope.Single(account) }
        val all = AccountScope.Group(type = AccountGroupType.ALL, accounts = accounts)

        return buildList {
            add(notArchived)
            addAll(singleAccounts)
            add(all)
        }

    }
}
