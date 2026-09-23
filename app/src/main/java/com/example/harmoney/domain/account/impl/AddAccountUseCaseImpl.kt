package com.example.harmoney.domain.account.impl

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.account.api.repository.AccountRepository
import com.example.harmoney.domain.account.api.useCase.AddAccountUseCase
import com.example.harmoney.domain.account.models.AccountFailure
import com.example.harmoney.domain.models.Account

class AddAccountUseCaseImpl(private val repository: AccountRepository) : AddAccountUseCase {
    override suspend fun execute(account: Account): Resource<Unit, AccountFailure> {
        return repository.addAccount(account)
    }
}
