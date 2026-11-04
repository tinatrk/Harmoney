package com.example.harmoney.domain.statistics.api.repository

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.models.CategoriesSummary
import com.example.harmoney.domain.models.CategoryStatistics
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.Money
import com.example.harmoney.domain.models.StatisticsPeriod
import com.example.harmoney.domain.transaction.models.TransactionFailure
import kotlinx.coroutines.flow.Flow

interface StatisticsRepository {
    fun getCategoriesSummary(
        accountIds: List<Long>,
        categoryType: CategoryType,
        period: StatisticsPeriod
    ): Flow<Resource<CategoriesSummary, TransactionFailure>>

    fun getCategoriesTotal(
        accountIds: List<Long>,
        categoryType: CategoryType,
        period: StatisticsPeriod
    ): Flow<Resource<List<Money>, TransactionFailure>>

    fun getCategoryStatistics(
        accountIds: List<Long>,
        categoryType: CategoryType,
        period: StatisticsPeriod
    ): Flow<Resource<List<CategoryStatistics>, TransactionFailure>>
}
