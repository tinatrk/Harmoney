package com.example.harmoney.presentation.categoryStatistics.viewModel

import com.example.harmoney.R
import com.example.harmoney.base.BaseViewModel
import com.example.harmoney.base.ResourceProvider
import com.example.harmoney.domain.models.CategoryStatistics
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.presentation.categoryStatistics.models.CategoryStatisticsAction
import com.example.harmoney.presentation.categoryStatistics.models.CategoryStatisticsEvent
import com.example.harmoney.presentation.categoryStatistics.models.CategoryStatisticsState
import com.example.harmoney.presentation.converters.CategoryStatisticsUiConverter
import com.example.harmoney.presentation.converters.NumbersFormatter
import com.example.harmoney.presentation.models.PieChartItem
import com.example.harmoney.presentation.models.StatisticPeriod
import kotlinx.coroutines.flow.update

class CategoryStatisticsViewModel(
    private val test: TestDataSource,
    private val categoryConverter: CategoryStatisticsUiConverter,
    private val numbersFormatter: NumbersFormatter,
    private val resourceProvider: ResourceProvider
) :
    BaseViewModel<CategoryStatisticsEvent, CategoryStatisticsAction, CategoryStatisticsState>(
        state = CategoryStatisticsState()
    ) {
    override val tag: String = CategoryStatisticsViewModel::class.java.simpleName ?: ""

    init {
        //TODO() считать тему из shared preferences
        //TODO() считать валюту из shared preferences
        _state.update {
            val categories =
                test.getCategoriesForStatistics(
                    it.selectedStatisticsPeriod,
                    it.categoryType
                )
            val formattedCategories = categoryConverter.map(categories, it.currency)

            val total = categories.sumOf { category -> category.totalAmount }

            it.copy(
                statisticsDate = test.getStatisticsDate(it.selectedStatisticsPeriod),
                categories = formattedCategories,
                pieChartCategories = getPieChartCategories(
                    categories = categories,
                    categoriesAmountString = formattedCategories.map { category -> category.totalAmount }
                ),
                total = numbersFormatter.toStringWithCurrency(
                    number = total,
                    decimalPlaces = TWO_DECIMAL_PLACES,
                    currency = it.currency,
                    isNeededThousandSeparator = true
                ),
                currentBalance = numbersFormatter.toStringWithCurrency(
                    number = test.getBalance(),
                    decimalPlaces = TWO_DECIMAL_PLACES,
                    currency = it.currency,
                    isNeededThousandSeparator = true

                )
            )
        }
    }

    override fun obtainEvent(event: CategoryStatisticsEvent) {
        when (event) {
            is CategoryStatisticsEvent.OnTabClick -> onTabClick(event.categoryType)
            is CategoryStatisticsEvent.OnSettingsIconClick -> onNavigateToSettings()
            is CategoryStatisticsEvent.OnTransactionListIconClick -> onNavigateToTransactionList(
                null
            )

            is CategoryStatisticsEvent.OnFloatingButtonClick -> onNavigateToTransaction()
            is CategoryStatisticsEvent.OnCategoryClick -> onNavigateToTransactionList(
                event.categoryId
            )

            is CategoryStatisticsEvent.OnStatisticsPeriodClick -> {
                onStatisticsPeriodClick(event.newPeriodId)
            }

            is CategoryStatisticsEvent.OnChangeTheme -> onThemeChanged()

            is CategoryStatisticsEvent.OnFirstDayMonthClick -> onFirstDayMonthClick()
            is CategoryStatisticsEvent.OnFirstDayMonthDialogConfirm -> onFirstDayMonthDialogConfirm()
            is CategoryStatisticsEvent.OnFirstDayMonthDialogDismiss -> onFirstDayMonthDialogDismiss()
            is CategoryStatisticsEvent.OnFirstDayMonthTextChanged -> {
                onFirstDayMonthTextChanged(event.newText)
            }

            is CategoryStatisticsEvent.OnCurrencyChanged -> onCurrencyChanged(event.newCurrency)

            is CategoryStatisticsEvent.OnCategoryListClick -> {
                onNavigateToCategoryList()
            }
        }
    }

    private fun onTabClick(newCategoryType: CategoryType) {
        if (_state.value.categoryType.id != newCategoryType.id) {
            _state.update {
                val categories =
                    test.getCategoriesForStatistics(
                        statisticPeriod = it.selectedStatisticsPeriod,
                        categoryType = newCategoryType
                    )
                val formattedCategories = categoryConverter.map(categories, it.currency)

                val total = categories.sumOf { category -> category.totalAmount }

                it.copy(
                    categoryType = newCategoryType,
                    selectedTabIndex = newCategoryType.ordinal,
                    categories = categoryConverter.map(categories, it.currency),
                    pieChartCategories = getPieChartCategories(
                        categories,
                        formattedCategories.map { category ->
                            category.totalAmount
                        }
                    ),
                    total = numbersFormatter.toStringWithCurrency(
                        number = total,
                        decimalPlaces = TWO_DECIMAL_PLACES,
                        currency = it.currency,
                        isNeededThousandSeparator = true
                    ),
                )
            }
        }
    }

    private fun onNavigateToSettings() {
        _action.tryEmit(CategoryStatisticsAction.NavigateToSettings)
    }

    private fun onNavigateToTransactionList(categoryId: Long?) {
        _action.tryEmit(CategoryStatisticsAction.NavigateToTransactionList(categoryId))
    }

    private fun onNavigateToTransaction() {
        _action.tryEmit(CategoryStatisticsAction.NavigateToTransaction)
    }

    private fun onNavigateToCategoryList() {
        _action.tryEmit(CategoryStatisticsAction.NavigateToCategoryList)
    }

    private fun onStatisticsPeriodClick(newPeriodId: Long) {
        _state.update {
            val newPeriod = StatisticPeriod.fromId(newPeriodId)
            val categories = test.getCategoriesForStatistics(
                statisticPeriod = newPeriod,
                categoryType = it.categoryType
            )
            val formattedCategories = categoryConverter.map(categories, it.currency)
            val total = categories.sumOf { category -> category.totalAmount }

            it.copy(
                categories = categoryConverter.map(categories, it.currency),
                pieChartCategories = getPieChartCategories(
                    categories,
                    formattedCategories.map { category ->
                        category.totalAmount
                    }
                ),
                total = numbersFormatter.toStringWithCurrency(
                    number = total,
                    decimalPlaces = TWO_DECIMAL_PLACES,
                    currency = it.currency,
                    isNeededThousandSeparator = true
                ),
                statisticsDate = test.getStatisticsDate(newPeriod),
                selectedStatisticsPeriod = newPeriod
            )
        }
    }

    private fun getPieChartCategories(
        categories: List<CategoryStatistics>,
        categoriesAmountString: List<String>,
    ): List<PieChartItem> {
        val startAngle = -90f
        val gapAngle = 2f
        val minAngle = 0f
        val maxAngle = 360f

        val total = categories.sumOf { it.totalAmount }.toFloat()
        val pieChartItems: MutableList<PieChartItem> = mutableListOf()

        var curStartAngle = startAngle
        for (i in categories.indices) {
            val rawSweep = (categories[i].totalAmount.toFloat() / total) * maxAngle
            val sweepAngle = (rawSweep - gapAngle).coerceAtLeast(minAngle)

            pieChartItems.add(
                PieChartItem(
                    value = categoriesAmountString[i],
                    colorValue = categories[i].category.icon.colors.background,
                    startAngle = curStartAngle,
                    sweepAngle = sweepAngle
                )
            )

            curStartAngle += rawSweep
        }

        return pieChartItems
    }

    private fun onThemeChanged() {
        _state.update {
            it.copy(
                isThemeDark = !_state.value.isThemeDark
            )
        }
        //TODO() в будущем поменять логику на shared preferences
    }

    private fun onFirstDayMonthClick() {
        if (!state.value.isOpenedFirstDayMonthDialog) {
            _state.update {
                it.copy(
                    isOpenedFirstDayMonthDialog = true
                )
            }
        }
    }

    private fun onFirstDayMonthDialogConfirm() {
        val firstDay = state.value.firstDayMonthText.toIntOrNull()
        val isFirstDayCorrect = if (firstDay != null) {
            firstDay in MIN_FIRST_DAY_MONTH..MAX_FIRST_DAY_MONTH
        } else false

        if (isFirstDayCorrect) {
            _state.update {
                it.copy(
                    firstDayMonth = firstDay ?: it.firstDayMonth,
                    isFirstDayMonthError = false,
                    firstDayMonthSupportText = "",
                    isOpenedFirstDayMonthDialog = false
                )
            }
            //TODO() Добавить логику изменения первого дня месяца
        }
    }

    private fun onFirstDayMonthDialogDismiss() {
        _state.update {
            it.copy(
                firstDayMonthText = it.firstDayMonth.toString(),
                isFirstDayMonthError = false,
                firstDayMonthSupportText = "",
                isOpenedFirstDayMonthDialog = false
            )
        }
    }

    private fun onFirstDayMonthTextChanged(newText: String) {
        val firstDay = newText.toIntOrNull()
        val isFirstDayCorrect = if (firstDay != null) {
            firstDay in MIN_FIRST_DAY_MONTH..MAX_FIRST_DAY_MONTH
        } else false

        _state.update {
            it.copy(
                firstDayMonthText = newText,
                isFirstDayMonthError = !isFirstDayCorrect,
                firstDayMonthSupportText = if (isFirstDayCorrect) {
                    ""
                } else {
                    resourceProvider.getString(
                        R.string.error_incorrect_first_day_month_pattern,
                        MIN_FIRST_DAY_MONTH,
                        MAX_FIRST_DAY_MONTH
                    )
                }
            )
        }
    }

    private fun onCurrencyChanged(newCurrency: Currency) {
        // TODO() Добавить логику переключения валюты
    }

    private companion object {
        const val TWO_DECIMAL_PLACES = 2
        const val ZERO_DECIMAL_PLACES = 0
        const val MIN_FIRST_DAY_MONTH = 1
        const val MAX_FIRST_DAY_MONTH = 28
    }
}
