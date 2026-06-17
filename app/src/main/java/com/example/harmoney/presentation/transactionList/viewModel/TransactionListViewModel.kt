package com.example.harmoney.presentation.transactionList.viewModel

import com.example.harmoney.base.BaseViewModel
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.models.StatisticsPeriod
import com.example.harmoney.presentation.converters.NumbersFormatter
import com.example.harmoney.presentation.converters.OneDayTransactionsUiConverter
import com.example.harmoney.presentation.converters.TransactionsFilterUiConverter
import com.example.harmoney.presentation.models.DecimalPlaces
import com.example.harmoney.presentation.models.TransactionsFilterUi
import com.example.harmoney.presentation.test.TestDataSource
import com.example.harmoney.presentation.transactionList.models.TransactionListAction
import com.example.harmoney.presentation.transactionList.models.TransactionListEvent
import com.example.harmoney.presentation.transactionList.models.TransactionListState
import kotlinx.coroutines.flow.update

@Suppress("detekt:LongParameterList", "detekt:TooManyFunctions")
class TransactionListViewModel(
    categoryType: CategoryType,
    statisticsPeriod: StatisticsPeriod,
    categoryId: Long?,
    private val test: TestDataSource,
    private val oneDayTransactionsUiConverter: OneDayTransactionsUiConverter,
    private val numberFormatter: NumbersFormatter,
    private val transactionsFilterUiConverter: TransactionsFilterUiConverter
) : BaseViewModel<TransactionListEvent, TransactionListAction, TransactionListState>(
    state = TransactionListState(
        selectedCategoryType = categoryType,
        selectedTabIndex = categoryType.ordinal,
        selectedStatisticsPeriod = statisticsPeriod
    )
) {
    override val tag: String = TransactionListViewModel::class.java.simpleName ?: ""
    private val currency: Currency
    private val selectedFilters: MutableList<Long>

    init {
        val filters = transactionsFilterUiConverter
            .map(filters = test.getTransactionFilters(state.value.selectedCategoryType))
        val filter = categoryId?.let { filters.find { it.id == categoryId } } ?: filters.first()

        selectedFilters = if (filters.isNotEmpty()) {
            CategoryType.entries.map { filters.first().id }.toMutableList()
        } else {
            CategoryType.entries.map { ZERO_ID }.toMutableList()
        }
        selectedFilters[state.value.selectedCategoryType.ordinal] = filter.id

        val transactions = test.getTransactionList(
            statisticsPeriod = state.value.selectedStatisticsPeriod,
            categoryType = state.value.selectedCategoryType,
            filterId = filter.id
        )
        val balance = test.getBalance()
        // TODO() в будущем считывать валюту из sharedPreferences
        currency = Currency.RUB
        val totalAmount = transactions.sumOf { it.totalAmount }

        writableState.update {
            it.copy(
                currentBalance = numberFormatter.toStringWithCurrency(
                    number = balance,
                    decimalPlaces = DecimalPlaces.MONEY_DISPLAY,
                    currency = currency,
                    isNeededThousandSeparator = true
                ),
                statisticsDate = test.getStatisticsDate(it.selectedStatisticsPeriod),
                totalAmount = numberFormatter.toStringWithCurrency(
                    number = totalAmount,
                    decimalPlaces = DecimalPlaces.MONEY_DISPLAY,
                    currency = currency,
                    isNeededThousandSeparator = true
                ),
                oneDayTransactionsList = oneDayTransactionsUiConverter
                    .map(days = transactions, currency = currency),
                transactionsFilters = filters,
                isFilterMenuOpened = false,
                selectedFilter = filter
            )
        }
    }

    override fun obtainEvent(event: TransactionListEvent) {
        when (event) {
            is TransactionListEvent.OnBackClick -> onNavigateBack()
            is TransactionListEvent.OnTabClick -> onTabClick(newCategoryType = event.categoryType)
            is TransactionListEvent.OnStatisticsPeriodClick -> {
                onStatisticPeriodClick(event.newPeriod)
            }

            is TransactionListEvent.OnFloatingButtonClick -> onCreateTransaction()
            is TransactionListEvent.OnTransactionClick -> onOpenTransaction(event.transactionId)

            is TransactionListEvent.OnFilterMenuClick -> onFilterMenuClick()
            is TransactionListEvent.OnFilterMenuChanged -> onFilterMenuChanged(event.filter)
            is TransactionListEvent.OnFilterMenuDismiss -> onFilterMenuDismiss()
        }
    }

    private fun onNavigateBack() {
        writableAction.tryEmit(TransactionListAction.NavigateBack)
    }

    private fun onTabClick(newCategoryType: CategoryType) {
        if (writableState.value.selectedCategoryType.id != newCategoryType.id) {
            val filters = transactionsFilterUiConverter.map(
                filters = test.getTransactionFilters(newCategoryType)
            )
            val curFilter = filters.find {
                it.id == selectedFilters[newCategoryType.ordinal]
            } ?: filters.first()
            selectedFilters[newCategoryType.ordinal] = curFilter.id

            val transactions = test.getTransactionList(
                statisticsPeriod = state.value.selectedStatisticsPeriod,
                categoryType = newCategoryType,
                filterId = curFilter.id
            )
            val totalAmount = transactions.sumOf { it.totalAmount }
            writableState.update {
                it.copy(
                    selectedCategoryType = newCategoryType,
                    selectedTabIndex = newCategoryType.ordinal,
                    totalAmount = numberFormatter.toStringWithCurrency(
                        number = totalAmount,
                        decimalPlaces = DecimalPlaces.MONEY_DISPLAY,
                        currency = currency,
                        isNeededThousandSeparator = true
                    ),
                    oneDayTransactionsList = oneDayTransactionsUiConverter.map(
                        days = transactions,
                        currency = currency
                    ),
                    transactionsFilters = filters,
                    selectedFilter = curFilter
                )
            }
        }
    }

    private fun onStatisticPeriodClick(newPeriod: StatisticsPeriod) {
        if (newPeriod.id != state.value.selectedStatisticsPeriod.id) {
            val transactions = test.getTransactionList(
                statisticsPeriod = newPeriod,
                categoryType = state.value.selectedCategoryType,
                filterId = state.value.selectedFilter.id
            )
            val totalAmount = transactions.sumOf { it.totalAmount }

            writableState.update {
                it.copy(
                    statisticsDate = test.getStatisticsDate(statisticsPeriod = newPeriod),
                    selectedStatisticsPeriod = newPeriod,
                    totalAmount = numberFormatter.toStringWithCurrency(
                        number = totalAmount,
                        decimalPlaces = DecimalPlaces.MONEY_DISPLAY,
                        currency = currency,
                        isNeededThousandSeparator = true
                    ),
                    oneDayTransactionsList = oneDayTransactionsUiConverter.map(
                        days = transactions,
                        currency = currency
                    ),
                )
            }
        }
    }

    private fun onCreateTransaction() {
        val filterId: Long? = if (state.value.selectedFilter.id > ZERO_ID) {
            state.value.selectedFilter.id
        } else {
            null
        }
        writableAction.tryEmit(
            TransactionListAction.NavigateToCreatingTransaction(filterId)
        )
    }

    private fun onOpenTransaction(transactionId: Long?) {
        writableAction.tryEmit(
            TransactionListAction.NavigateToOpeningTransaction(transactionId)
        )
    }

    private fun onFilterMenuClick() {
        writableState.update {
            it.copy(isFilterMenuOpened = !state.value.isFilterMenuOpened)
        }
    }

    private fun onFilterMenuChanged(filter: TransactionsFilterUi) {
        if (state.value.selectedFilter.id != filter.id) {
            val transactions = test.getTransactionList(
                statisticsPeriod = state.value.selectedStatisticsPeriod,
                categoryType = state.value.selectedCategoryType,
                filterId = filter.id
            )
            val totalAmount = transactions.sumOf { it.totalAmount }
            selectedFilters[state.value.selectedCategoryType.ordinal] = filter.id

            writableState.update {
                it.copy(
                    selectedFilter = filter,
                    isFilterMenuOpened = false,
                    oneDayTransactionsList = oneDayTransactionsUiConverter.map(
                        days = transactions,
                        currency = currency
                    ),
                    totalAmount = numberFormatter.toStringWithCurrency(
                        number = totalAmount,
                        decimalPlaces = DecimalPlaces.MONEY_DISPLAY,
                        currency = currency,
                        isNeededThousandSeparator = true
                    )
                )
            }
        } else {
            onFilterMenuDismiss()
        }
    }

    private fun onFilterMenuDismiss() {
        writableState.update { it.copy(isFilterMenuOpened = false) }
    }

    private companion object {
        const val ZERO_ID = 0L
    }
}
