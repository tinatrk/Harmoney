package com.example.harmoney.domain.account.impl

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.account.api.repository.AccountRepository
import com.example.harmoney.domain.account.api.useCase.GetAccountUseCase
import com.example.harmoney.domain.account.models.AccountFailure
import com.example.harmoney.domain.models.Account

class GetAccountUseCaseImpl(private val repository: AccountRepository) : GetAccountUseCase {
    override suspend fun execute(accountId: Long): Resource<Account, AccountFailure> {
        return repository.getAccount(accountId)
    }
}
