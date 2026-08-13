package com.example.harmoney.domain.transaction.api.useCase

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.TransactionFilter
import com.example.harmoney.domain.transaction.models.TransactionFailure
import kotlinx.coroutines.flow.Flow

interface GetFilterListUseCase {
    fun execute(categoryType: CategoryType):
            Flow<Resource<List<TransactionFilter>, TransactionFailure>>
}
