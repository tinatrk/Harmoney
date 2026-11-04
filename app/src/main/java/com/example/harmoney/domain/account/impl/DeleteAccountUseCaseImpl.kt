package com.example.harmoney.domain.account.impl

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.account.api.repository.AccountRepository
import com.example.harmoney.domain.account.api.useCase.DeleteAccountUseCase
import com.example.harmoney.domain.account.models.AccountFailure
import com.example.harmoney.domain.models.Account

class DeleteAccountUseCaseImpl(private val repository: AccountRepository) : DeleteAccountUseCase {
    override suspend fun execute(account: Account): Resource<Unit, AccountFailure> {
        return repository.deleteAccount(account)
    }
}
