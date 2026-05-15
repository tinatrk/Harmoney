package com.example.harmoney.presentation.transactionList.models

import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.StatisticPeriod

data class TransactionListState(
    val currentBalance: String = "0.00",
    val categoryId: Long? = null,
    val categoryTypes: List<CategoryType> = CategoryType.entries,
    val selectedCategoryType: CategoryType = CategoryType.Expenses,
    val selectedTabIndex: Int = CategoryType.Expenses.ordinal,

    val statisticsPeriods: List<StatisticPeriod> = StatisticPeriod.entries,
    val statisticsDate: String = "",
    val selectedStatisticsPeriod: StatisticPeriod = StatisticPeriod.CURRENT_MONTH,

    // что-то про фильтрацию
)
