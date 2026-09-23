package com.example.harmoney.domain.account.impl

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.account.api.repository.AccountRepository
import com.example.harmoney.domain.account.api.useCase.CheckAccountAlreadyExistsUseCase
import com.example.harmoney.domain.account.models.AccountFailure
import com.example.harmoney.domain.models.Currency

class CheckAccountAlreadyExistsUseCaseImpl(private val repository: AccountRepository) :
    CheckAccountAlreadyExistsUseCase {
    override suspend fun execute(
        accountName: String,
        currency: Currency
    ): Resource<Boolean, AccountFailure> {
        return repository.checkAccountAlreadyExists(accountName, currency)
    }
}
