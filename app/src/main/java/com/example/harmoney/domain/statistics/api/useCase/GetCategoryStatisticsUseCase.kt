package com.example.harmoney.domain.statistics.api.useCase

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.models.CategoryStatistics
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.StatisticsPeriod
import com.example.harmoney.domain.transaction.models.TransactionFailure
import kotlinx.coroutines.flow.Flow

interface GetCategoryStatisticsUseCase {

    operator fun invoke(
        accountIds: List<Long>,
        categoryType: CategoryType,
        period: StatisticsPeriod,
    ): Flow<Resource<List<CategoryStatistics>, TransactionFailure>>
}
