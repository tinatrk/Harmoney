package com.example.harmoney.presentation.categoryStatistics.models

import androidx.compose.runtime.Stable
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.models.StatisticsPeriod
import com.example.harmoney.presentation.models.CategoryStatisticsUi
import com.example.harmoney.presentation.models.PieChartItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Stable
data class CategoryStatisticsState(
    val currentBalance: String = "0.00",

    val selectedCategoryType: CategoryType = CategoryType.EXPENSES,
    val selectedTabIndex: Int = CategoryType.EXPENSES.ordinal,

    val categories: ImmutableList<CategoryStatisticsUi> = persistentListOf(),
    val pieChartCategories: ImmutableList<PieChartItem> = persistentListOf(),
    val total: String = "0.00",

    val statisticsDate: String = "",
    val selectedStatisticsPeriod: StatisticsPeriod = StatisticsPeriod.CURRENT_MONTH,

    val isThemeDark: Boolean = false,

    val firstDayMonth: Int = 1,
    val isOpenedFirstDayMonthDialog: Boolean = false,
    val firstDayMonthText: String = firstDayMonth.toString(),
    val firstDayMonthError: FirstDayMonthError = FirstDayMonthError.None,

    val currency: Currency = Currency.RUB,
    val isCurrencyMenuOpened: Boolean = false
)
