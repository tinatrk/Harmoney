package com.example.harmoney.domain.statistics.api.repository

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.models.Account
import com.example.harmoney.domain.models.CategoriesSummary
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.StatisticsPeriod
import com.example.harmoney.domain.transaction.models.TransactionFailure
import kotlinx.coroutines.flow.Flow

interface StatisticsRepository {
    fun getCategoriesSummary(
        accounts: List<Account>,
        categoryType: CategoryType,
        period: StatisticsPeriod
    ): Flow<Resource<CategoriesSummary, TransactionFailure>>
}
