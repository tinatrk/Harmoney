package com.example.harmoney.domain.statistics.impl

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.models.CategoriesSummary
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.StatisticsPeriod
import com.example.harmoney.domain.statistics.api.repository.StatisticsRepository
import com.example.harmoney.domain.statistics.api.useCase.GetCategoriesSummaryUseCase
import com.example.harmoney.domain.transaction.models.TransactionFailure
import kotlinx.coroutines.flow.Flow

class GetCategoriesSummaryUseCaseImpl(private val repository: StatisticsRepository) :
    GetCategoriesSummaryUseCase {
    override fun execute(
        accountIds: List<Long>,
        categoryType: CategoryType,
        period: StatisticsPeriod
    ): Flow<Resource<CategoriesSummary, TransactionFailure>> {
        return repository.getCategoriesSummary(accountIds, categoryType, period)
    }
}
