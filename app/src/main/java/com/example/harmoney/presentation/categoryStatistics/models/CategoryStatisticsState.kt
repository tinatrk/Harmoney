package com.example.harmoney.presentation.categoryStatistics.models

import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.presentation.models.CategoryStatisticsUi
import com.example.harmoney.presentation.models.PieChartItem
import com.example.harmoney.presentation.models.StatisticPeriod

data class CategoryStatisticsState(
    val currentBalance: String = "0,00",
    val categoryType: CategoryType = CategoryType.Expenses,
    val selectedTabIndex: Int = CategoryType.Expenses.ordinal,

    val categories: List<CategoryStatisticsUi> = emptyList(),
    val pieChartCategories: List<PieChartItem> = emptyList(),
    val total: String = "0,00",

    val statisticsPeriods: List<StatisticPeriod> = StatisticPeriod.entries,
    val statisticsDate: String = "",
    val selectedStatisticsPeriod: StatisticPeriod = StatisticPeriod.CURRENT_MONTH,

    val isThemeDark: Boolean = false,

    val firstDayMonth: Int = 1,
    val firstDayMonthDialogOpen: Boolean = false,

    val currency: Currency = Currency.USD
    // выпадающий список выбора валюты?
)
