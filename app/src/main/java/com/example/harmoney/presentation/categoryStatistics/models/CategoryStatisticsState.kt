package com.example.harmoney.presentation.categoryStatistics.models

import androidx.compose.runtime.Stable
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.models.StatisticsPeriodType
import com.example.harmoney.presentation.models.CategoryStatisticsContentError
import com.example.harmoney.presentation.models.ContentState
import com.example.harmoney.presentation.models.StatisticsPeriodUi

@Stable
data class CategoryStatisticsState(
    val accountContent: ContentState<AccountContent, CategoryStatisticsContentError> =
        ContentState.Idle,
    val isAccountMenuOpened: Boolean = false,

    val selectedCategoryType: CategoryType = CategoryType.EXPENSES,
    val selectedTabIndex: Int = CategoryType.EXPENSES.ordinal,

    val selectedStatisticsPeriod: StatisticsPeriodUi = StatisticsPeriodUi(
        type = StatisticsPeriodType.CURRENT_MONTH, date = ""
    ),

    val statisticsContent: ContentState<CategoryStatisticsContent, CategoryStatisticsContentError> =
        ContentState.Idle,

    val firstDayMonth: Int = 1,
    val firstDayMonthText: String = firstDayMonth.toString(),
    val isOpenedFirstDayMonthDialog: Boolean = false,
    val firstDayMonthError: FirstDayMonthError = FirstDayMonthError.None,

    val reportingCurrency: Currency = Currency.EUR,
)
