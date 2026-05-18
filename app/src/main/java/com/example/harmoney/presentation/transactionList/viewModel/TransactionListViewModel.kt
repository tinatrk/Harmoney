package com.example.harmoney.presentation.transactionList.viewModel

import com.example.harmoney.base.BaseViewModel
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.presentation.categoryStatistics.viewModel.CategoryStatisticsViewModel
import com.example.harmoney.presentation.converters.NumbersFormatter
import com.example.harmoney.presentation.converters.OneDayTransactionsUiConverter
import com.example.harmoney.presentation.test.TestDataSource
import com.example.harmoney.presentation.transactionList.models.TransactionListAction
import com.example.harmoney.presentation.transactionList.models.TransactionListEvent
import com.example.harmoney.presentation.transactionList.models.TransactionListState
import kotlinx.coroutines.flow.update

class TransactionListViewModel(
    categoryId: Long?,
    private val test: TestDataSource,
    private val oneDayTransactionsUiConverter: OneDayTransactionsUiConverter,
    numberFormatter: NumbersFormatter
) : BaseViewModel<TransactionListEvent, TransactionListAction, TransactionListState>(
    TransactionListState()
) {
    override val tag: String = CategoryStatisticsViewModel::class.java.simpleName ?: ""
    private val currency: Currency

    init {
        val transactions = test.getTransactionList(
            statisticsPeriod = state.value.selectedStatisticsPeriod,
            categoryType = state.value.selectedCategoryType,
            categoryId = categoryId
        )
        val balance = test.getBalance()
        // TODO() в будущем считывать валюту из sharedPreferences
        currency = Currency.RUB

        writableState.update {
            it.copy(
                currentBalance = numberFormatter.toStringWithCurrency(
                    number = balance,
                    decimalPlaces = TWO_DECIMAL_PLACES,
                    currency = currency,
                    isNeededThousandSeparator = true
                ),
                categoryId = categoryId,
                statisticsDate = test.getStatisticsDate(it.selectedStatisticsPeriod)
            )
        }
    }

    override fun obtainEvent(event: TransactionListEvent) {
        when (event) {
            is TransactionListEvent.OnBackClick -> onNavigateBack()
            is TransactionListEvent.OnTabClick -> onTabClick(event.categoryType)
            is TransactionListEvent.OnStatisticsPeriodClick -> {
                onStatisticPeriodClick(event.newPeriodId)
            }

            is TransactionListEvent.OnFloatingButtonClick -> onCreateTransaction(event.categoryId)
            is TransactionListEvent.OnTransactionClick -> onOpenTransaction(event.transactionId)
        }
    }

    private fun onNavigateBack() {
        writableAction.tryEmit(TransactionListAction.NavigateBack)
    }

    private fun onTabClick(newCategoryType: CategoryType) {
        if (writableState.value.selectedCategoryType.id != newCategoryType.id) {
            writableState.update {
                it.copy(
                    selectedCategoryType = newCategoryType,
                    selectedTabIndex = newCategoryType.ordinal,
                )
            }
        }
    }

    private fun onStatisticPeriodClick(newPeriodId: Long) {

    }

    private fun onCreateTransaction(categoryId: Long?) {
        writableAction.tryEmit(TransactionListAction.NavigateToCreatingTransaction(categoryId))
    }

    private fun onOpenTransaction(transactionId: Long?) {
        writableAction.tryEmit(TransactionListAction.NavigateToOpeningTransaction(transactionId))
    }

    private companion object {
        const val TWO_DECIMAL_PLACES = 2
    }
}
