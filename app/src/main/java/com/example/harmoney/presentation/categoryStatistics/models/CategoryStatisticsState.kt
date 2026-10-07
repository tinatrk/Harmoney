package com.example.harmoney.presentation.categoryStatistics.models

import androidx.compose.runtime.Stable
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.models.StatisticsPeriodType
import com.example.harmoney.presentation.models.AccountScopeUi
import com.example.harmoney.presentation.models.CategoryStatisticsUi
import com.example.harmoney.presentation.models.PieChartItem
import com.example.harmoney.presentation.models.StatisticsPeriodUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Stable
data class CategoryStatisticsState(
    val isAccountsLoading: Boolean = true,
    val isStatisticsLoading: Boolean = true,
    val isStatisticsError: Boolean = false,

    val accountScopes: ImmutableList<AccountScopeUi> = persistentListOf(),
    val selectedAccountScope: AccountScopeUi? = null,
    val accountsExchangeRateStatus: String? = null,

    val selectedCategoryType: CategoryType = CategoryType.EXPENSES,
    val selectedTabIndex: Int = CategoryType.EXPENSES.ordinal,

    val selectedStatisticsPeriod: StatisticsPeriodUi = StatisticsPeriodUi(
        type = StatisticsPeriodType.CURRENT_MONTH, date = ""
    ),

    val categories: ImmutableList<CategoryStatisticsUi> = persistentListOf(),
    val pieChartCategories: ImmutableList<PieChartItem> = persistentListOf(),
    val totalByCategoryType: String = "0.00",
    val totalOriginalsByCategoryType: String = "0.00",

    val firstDayMonth: Int = 1,
    val firstDayMonthText: String = firstDayMonth.toString(),
    val isOpenedFirstDayMonthDialog: Boolean = false,
    val firstDayMonthError: FirstDayMonthError = FirstDayMonthError.None,

    val reportingCurrency: Currency = Currency.EUR,
)
