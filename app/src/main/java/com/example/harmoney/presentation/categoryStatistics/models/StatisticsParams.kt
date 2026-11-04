package com.example.harmoney.presentation.categoryStatistics.models

import com.example.harmoney.domain.models.AccountScope
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.StatisticsPeriod

data class StatisticsParams(
    val accountScope: AccountScope,
    val categoryType: CategoryType,
    val period: StatisticsPeriod,
)
