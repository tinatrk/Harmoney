package com.example.harmoney.core.session

import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.StatisticsPeriodType

data class SessionState(
    val statisticsPeriodType: StatisticsPeriodType = StatisticsPeriodType.CURRENT_MONTH,
    val categoryType: CategoryType = CategoryType.EXPENSES
)
