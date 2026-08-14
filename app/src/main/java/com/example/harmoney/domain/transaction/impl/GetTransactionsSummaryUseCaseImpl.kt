package com.example.harmoney.domain.transaction.impl

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.StatisticsPeriod
import com.example.harmoney.domain.models.TransactionFilter
import com.example.harmoney.domain.models.TransactionsSummary
import com.example.harmoney.domain.transaction.api.repository.TransactionRepository
import com.example.harmoney.domain.transaction.api.useCase.GetTransactionsSummaryUseCase
import com.example.harmoney.domain.transaction.models.TransactionFailure
import kotlinx.coroutines.flow.Flow

class GetTransactionsSummaryUseCaseImpl(private val repository: TransactionRepository) :
    GetTransactionsSummaryUseCase {
    override fun execute(
        categoryType: CategoryType,
        period: StatisticsPeriod,
        filter: TransactionFilter
    ): Flow<Resource<TransactionsSummary, TransactionFailure>> {
        return repository.getTransactionsSummary(categoryType, period, filter)
    }
}
