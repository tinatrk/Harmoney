package com.example.harmoney.domain.account.impl

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.account.api.repository.AccountRepository
import com.example.harmoney.domain.account.api.useCase.UpdateAccountUseCase
import com.example.harmoney.domain.account.models.AccountFailure
import com.example.harmoney.domain.models.Account

class UpdateAccountUseCaseImpl(private val repository: AccountRepository) : UpdateAccountUseCase {
    override suspend fun execute(account: Account): Resource<Unit, AccountFailure> {
        return repository.updateAccount(account)
    }
}
