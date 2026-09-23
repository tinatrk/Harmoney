package com.example.harmoney.domain.account.impl

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.account.api.repository.AccountRepository
import com.example.harmoney.domain.account.api.useCase.GetAccountListUseCase
import com.example.harmoney.domain.account.models.AccountFailure
import com.example.harmoney.domain.models.Account
import com.example.harmoney.domain.models.SortOption
import kotlinx.coroutines.flow.Flow

class GetAccountListUseCaseImpl(private val repository: AccountRepository) : GetAccountListUseCase {
    override fun execute(sortOption: SortOption): Flow<Resource<List<Account>, AccountFailure>> {
        return repository.getAccountList(sortOption)
    }
}
