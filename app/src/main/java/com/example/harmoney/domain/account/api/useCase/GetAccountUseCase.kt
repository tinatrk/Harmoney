package com.example.harmoney.domain.account.api.useCase

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.account.models.AccountFailure
import com.example.harmoney.domain.models.Account

interface GetAccountUseCase {
    suspend fun execute(accountId: Long): Resource<Account, AccountFailure>
}
