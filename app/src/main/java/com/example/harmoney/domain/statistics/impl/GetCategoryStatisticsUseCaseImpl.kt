package com.example.harmoney.domain.statistics.impl

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.models.CategoryStatistics
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.StatisticsPeriod
import com.example.harmoney.domain.statistics.api.repository.StatisticsRepository
import com.example.harmoney.domain.statistics.api.useCase.GetCategoryStatisticsUseCase
import com.example.harmoney.domain.transaction.models.TransactionFailure
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

class GetCategoryStatisticsUseCaseImpl(
    private val repository: StatisticsRepository,
) : GetCategoryStatisticsUseCase {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun invoke(
        accountIds: List<Long>,
        categoryType: CategoryType,
        period: StatisticsPeriod,
    ): Flow<Resource<List<CategoryStatistics>, TransactionFailure>> {
        return repository.getCategoryStatistics(accountIds, categoryType, period)
    }
}
