package com.example.harmoney.domain.account.api.useCase

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.account.models.AccountFailure
import com.example.harmoney.domain.models.Account

interface UpdateAccountUserOrderUseCase {
    suspend fun execute(
        from: Int,
        to: Int,
        oldAccounts: List<Account>
    ): Resource<Unit, AccountFailure>
}
