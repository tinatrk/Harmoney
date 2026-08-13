package com.example.harmoney.domain.transaction.impl

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.TransactionFilter
import com.example.harmoney.domain.transaction.api.repository.TransactionRepository
import com.example.harmoney.domain.transaction.api.useCase.GetFilterListUseCase
import com.example.harmoney.domain.transaction.models.TransactionFailure
import kotlinx.coroutines.flow.Flow

class GetFilterListUseCaseImpl(val repository: TransactionRepository) : GetFilterListUseCase {
    override fun execute(categoryType: CategoryType)
            : Flow<Resource<List<TransactionFilter>, TransactionFailure>> {
        return repository.getFilterList(categoryType)
    }
}
