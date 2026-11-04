package com.example.harmoney.presentation.categoryStatistics.models

import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.models.StatisticsPeriod

data class CategoryStatisticsSettings(
    val selectedCategoryType: CategoryType,
    val periods: List<StatisticsPeriod>,
    val selectedPeriod: StatisticsPeriod,
    val reportingCurrency: Currency,
    val firstDayMonth: Int,
)
