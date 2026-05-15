package com.example.harmoney.presentation.categoryStatistics.models

import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.presentation.models.CategoryStatisticsUi
import com.example.harmoney.presentation.models.PieChartItem
import com.example.harmoney.domain.models.StatisticPeriod

data class CategoryStatisticsState(
    val currentBalance: String = "0.00",
    val categoryTypes: List<CategoryType> = CategoryType.entries,
    val selectedCategoryType: CategoryType = CategoryType.Expenses,
    val selectedTabIndex: Int = CategoryType.Expenses.ordinal,

    val categories: List<CategoryStatisticsUi> = emptyList(),
    val pieChartCategories: List<PieChartItem> = emptyList(),
    val total: String = "0.00",

    val statisticsPeriods: List<StatisticPeriod> = StatisticPeriod.entries,
    val statisticsDate: String = "",
    val selectedStatisticsPeriod: StatisticPeriod = StatisticPeriod.CURRENT_MONTH,

    val isThemeDark: Boolean = false,

    val firstDayMonth: Int = 1,
    val isOpenedFirstDayMonthDialog: Boolean = false,
    val firstDayMonthText: String = firstDayMonth.toString(),
    val isFirstDayMonthError: Boolean = false,
    val firstDayMonthSupportText: String = "",

    val currency: Currency = Currency.RUB,
    val isCurrencyMenuOpened: Boolean = false
)
