package com.example.harmoney.domain.account.api.useCase

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.account.models.AccountFailure
import com.example.harmoney.domain.models.Account
import com.example.harmoney.domain.models.SortOption
import kotlinx.coroutines.flow.Flow

interface GetAccountListUseCase {
    fun execute(sortOption: SortOption): Flow<Resource<List<Account>, AccountFailure>>
}
