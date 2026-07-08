package com.example.harmoney.presentation.transactionList.models

import androidx.compose.runtime.Stable
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.StatisticsPeriodType
import com.example.harmoney.presentation.models.OneDayTransactionsUi
import com.example.harmoney.presentation.models.StatisticsPeriodUi
import com.example.harmoney.presentation.models.TransactionsFilterUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Stable
data class TransactionListState(
    val currentBalance: String = "0.00",

    val selectedCategoryType: CategoryType = CategoryType.EXPENSES,
    val selectedTabIndex: Int = CategoryType.EXPENSES.ordinal,

    val selectedStatisticsPeriod: StatisticsPeriodUi = StatisticsPeriodUi(
        type = StatisticsPeriodType.CURRENT_MONTH, date = ""
    ),

    val totalAmount: String = "0.00",
    val oneDayTransactionsList: ImmutableList<OneDayTransactionsUi> = persistentListOf(),

    val transactionsFilters: ImmutableList<TransactionsFilterUi> = persistentListOf(),
    val isFilterMenuOpened: Boolean = false,
    val selectedFilter: TransactionsFilterUi = TransactionsFilterUi(id = -1, name = "")
)
