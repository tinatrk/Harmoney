package com.example.harmoney.domain.account.api.useCase

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.account.models.AccountFailure
import com.example.harmoney.domain.models.Currency

interface CheckAccountAlreadyExistsUseCase {
    suspend fun execute(accountName: String, currency: Currency): Resource<Boolean, AccountFailure>
}
