package com.example.harmoney.presentation.transactionList.viewModel

import androidx.lifecycle.viewModelScope
import com.example.harmoney.base.BaseViewModel
import com.example.harmoney.core.session.SessionStateHolder
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.models.StatisticsPeriodType
import com.example.harmoney.domain.settings.period.api.useCase.GetStatisticsPeriodsUseCase
import com.example.harmoney.presentation.converters.NumbersFormatter
import com.example.harmoney.presentation.converters.OneDayTransactionsUiConverter
import com.example.harmoney.presentation.converters.StatisticsPeriodUiConverter
import com.example.harmoney.presentation.converters.TransactionFilterUiConverter
import com.example.harmoney.presentation.models.DecimalPlaces
import com.example.harmoney.presentation.models.TransactionFilterUi
import com.example.harmoney.presentation.test.TestDataSource
import com.example.harmoney.presentation.transactionList.models.TransactionListAction
import com.example.harmoney.presentation.transactionList.models.TransactionListEvent
import com.example.harmoney.presentation.transactionList.models.TransactionListState
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update

@Suppress("detekt:LongParameterList", "detekt:TooManyFunctions")
class TransactionListViewModel(
    private val sessionStateHolder: SessionStateHolder,
    categoryId: Long?,
    private val test: TestDataSource,
    private val oneDayTransactionsUiConverter: OneDayTransactionsUiConverter,
    private val numberFormatter: NumbersFormatter,
    private val transactionFilterUiConverter: TransactionFilterUiConverter,
    private val getStatisticsPeriodsUseCase: GetStatisticsPeriodsUseCase,
    private val statisticsPeriodUiConverter: StatisticsPeriodUiConverter,
) : BaseViewModel<TransactionListEvent, TransactionListAction, TransactionListState>(
    state = TransactionListState(
        selectedCategoryType = sessionStateHolder.state.value.categoryType,
        selectedTabIndex = sessionStateHolder.state.value.categoryType.ordinal,
    )
) {
    override val tag: String = TransactionListViewModel::class.java.simpleName ?: ""
    private val currency: Currency
    private val selectedFilters: MutableList<Long?>

    private val sessionFlow = sessionStateHolder.state
    private val periodsFlow = getStatisticsPeriodsUseCase()

    //private val filtersFlow = useCase (filters + selectedFilter)
    /*private val filtersFlow = sessionFlow.map { it.categoryType }.collectLatest {categoryType ->
        test.getTransactionFilters(categoryType)
    }*/
    // private balanceFlow = useCase

    init {
        // TODO() в будущем считывать валюту из sharedPreferences
        currency = Currency.RUB
        selectedFilters = mutableListOf()

        combine(
            sessionFlow,
            periodsFlow,
            //filterFlow
        ) { sessionData, periods ->//filter
            val selectedPeriod = periods.first { it.type == sessionData.statisticsPeriodType }
            val transactions = test.getTransactionList(
                selectedPeriod, sessionData.categoryType, null
            )

            val balance = test.getBalance()
            val totalAmount = transactions.sumOf { it.totalAmount }

            val filters = transactionFilterUiConverter // cчитывать из БД
                .map(filters = test.getTransactionFilters(state.value.selectedCategoryType))
            val filter = getFilterByCategoryId(categoryId, filters) /*categoryId?.let { id ->
                getCategoriesAsFilters(filters)
                    .find { it.id == id }
            } ?: TransactionFilterUi.All*/

            selectedFilters.addAll(
                CategoryType.entries.map { null }.toMutableList()
                /*if (getCategoriesAsFilters(filters).isNotEmpty()) {
                    CategoryType.entries.map {
                    getCategoriesAsFilters(filters).first().id
                    }.toMutableList()
                } else {
                    CategoryType.entries.map { null }.toMutableList()
                }*/
            )
            selectedFilters[state.value.selectedCategoryType.ordinal] = getFilterId(filter)

            writableState.update {
                it.copy(
                    currentBalance = numberFormatter.toStringWithCurrency(
                        number = balance,
                        decimalPlaces = DecimalPlaces.MONEY_DISPLAY,
                        currency = currency,
                        isNeededThousandSeparator = true
                    ),
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
                    selectedFilter = filter,
                    selectedCategoryType = sessionData.categoryType,
                    selectedTabIndex = sessionData.categoryType.ordinal,
                    selectedStatisticsPeriod = statisticsPeriodUiConverter.map(selectedPeriod)
                )
            }
        }.launchIn(viewModelScope)
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

    private fun getCategoriesAsFilters(filters: List<TransactionFilterUi>):
            List<TransactionFilterUi.CategoryUi> {
        return filters.filterIsInstance<TransactionFilterUi.CategoryUi>()
    }

    private fun getFilterId(filter: TransactionFilterUi): Long? {
        return when (filter) {
            is TransactionFilterUi.CategoryUi -> filter.id
            is TransactionFilterUi.All -> null
        }
    }

    private fun getFilterByCategoryId(
        categoryId: Long?,
        filters: List<TransactionFilterUi>
    ): TransactionFilterUi {
        return categoryId?.let { id ->
            filters
                .filterIsInstance<TransactionFilterUi.CategoryUi>()
                .find { it.id == id }
        } ?: TransactionFilterUi.All
    }

    private fun onNavigateBack() {
        writableAction.tryEmit(TransactionListAction.NavigateBack)
    }

    private fun onTabClick(newCategoryType: CategoryType) {
        if (writableState.value.selectedCategoryType.id != newCategoryType.id) {

            sessionStateHolder.setCategoryType(newCategoryType)
        }
    }

    private fun onStatisticPeriodClick(newPeriod: StatisticsPeriodType) {
        if (newPeriod.id != state.value.selectedStatisticsPeriod.type.id) {

            sessionStateHolder.setPeriodType(newPeriod)
        }
    }

    private fun onCreateTransaction() {
        val filterId: Long? = getFilterId(state.value.selectedFilter)
        /*if (getFilterId(state.value.selectedFilter) != null) {
            state.value.selectedFilter.id
        } else {
            null
        }*/
        writableAction.tryEmit(
            TransactionListAction.NavigateToCreatingTransaction(filterId)
        )
    }

    private fun onOpenTransaction(transactionId: Long?) {
        // Log.d("HarmAppTag", "TransactionListViewModel -> onOpenTransaction ->
        // transactionId = $transactionId")
        writableAction.tryEmit(
            TransactionListAction.NavigateToOpeningTransaction(transactionId)
        )
    }

    private fun onFilterMenuClick() {
        writableState.update {
            it.copy(isFilterMenuOpened = !state.value.isFilterMenuOpened)
        }
    }

    private fun onFilterMenuChanged(filter: TransactionFilterUi) {
        if (getFilterId(state.value.selectedFilter) != getFilterId(filter)) {

            // UseCase по изменению фильтра

            /*val transactions = test.getTransactionList(
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
            }*/
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
