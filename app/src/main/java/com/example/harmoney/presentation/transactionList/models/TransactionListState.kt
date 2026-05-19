package com.example.harmoney.presentation.transactionList.models

import androidx.compose.runtime.Stable
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.StatisticsPeriod
import com.example.harmoney.presentation.models.OneDayTransactionsUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Stable
data class TransactionListState(
    val currentBalance: String = "0.00",
    val categoryId: Long? = null,
    val categoryTypes: ImmutableList<CategoryType> = CategoryType.entries.toImmutableList(),
    val selectedCategoryType: CategoryType = CategoryType.Expenses,
    val selectedTabIndex: Int = CategoryType.Expenses.ordinal,

    val statisticsPeriods: ImmutableList<StatisticsPeriod> =
        StatisticsPeriod.entries.toImmutableList(),
    val statisticsDate: String = "",
    val selectedStatisticsPeriod: StatisticsPeriod = StatisticsPeriod.CURRENT_MONTH,

    val totalAmount: String = "0.00",
    val oneDayTransactionsList: ImmutableList<OneDayTransactionsUi> = persistentListOf()
    // что-то про фильтрацию
)
