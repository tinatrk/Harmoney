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
import com.example.harmoney.domain.models.StatisticPeriod
import com.example.harmoney.presentation.test.TestDataSource
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
        writableState.update {
            val categories =
                test.getCategoriesForStatistics(
                    it.selectedStatisticsPeriod,
                    it.selectedCategoryType
                )
            val formattedCategories = categoryConverter.map(categories, it.currency)

            val total = categories.sumOf { category -> category.totalAmount }

            it.copy(
                statisticsDate = test.getStatisticsDate(it.selectedStatisticsPeriod),
                categories = formattedCategories,
                pieChartCategories = getPieChartCategories(
                    categories = categories,
                    categoriesAmountString = formattedCategories
                        .map { category -> category.totalAmount }
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
            is CategoryStatisticsEvent.OnFirstDayMonthDialogConfirm -> {
                onFirstDayMonthDialogConfirm()
            }

            is CategoryStatisticsEvent.OnFirstDayMonthDialogDismiss -> {
                onFirstDayMonthDialogDismiss()
            }

            is CategoryStatisticsEvent.OnFirstDayMonthTextChanged -> {
                onFirstDayMonthTextChanged(event.newText)
            }

            is CategoryStatisticsEvent.OnCurrencySettingsClick -> onCurrencySettingsClick()
            is CategoryStatisticsEvent.OnCurrencyChanged -> onCurrencyChanged(event.newCurrency)
            is CategoryStatisticsEvent.OnCurrencyMenuDismiss -> onCurrencyMenuDismiss()

            is CategoryStatisticsEvent.OnCategoryListClick -> {
                onNavigateToCategoryList()
            }
        }
    }

    private fun onTabClick(newCategoryType: CategoryType) {
        if (writableState.value.selectedCategoryType.id != newCategoryType.id) {
            writableState.update {
                val categories =
                    test.getCategoriesForStatistics(
                        statisticPeriod = it.selectedStatisticsPeriod,
                        categoryType = newCategoryType
                    )
                val formattedCategories = categoryConverter.map(categories, it.currency)

                val total = categories.sumOf { category -> category.totalAmount }

                it.copy(
                    selectedCategoryType = newCategoryType,
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
        writableAction.tryEmit(CategoryStatisticsAction.NavigateToSettings)
    }

    private fun onNavigateToTransactionList(categoryId: Long?) {
        writableAction.tryEmit(CategoryStatisticsAction.NavigateToTransactionList(categoryId))
    }

    private fun onNavigateToTransaction() {
        writableAction.tryEmit(CategoryStatisticsAction.NavigateToTransaction)
    }

    private fun onNavigateToCategoryList() {
        writableAction.tryEmit(CategoryStatisticsAction.NavigateToCategoryList)
    }

    private fun onStatisticsPeriodClick(newPeriodId: Long) {
        writableState.update {
            val newPeriod = StatisticPeriod.fromId(newPeriodId)
            val categories = test.getCategoriesForStatistics(
                statisticPeriod = newPeriod,
                categoryType = it.selectedCategoryType
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
        val startAngle = START_ANGLE

        val total = categories.sumOf { it.totalAmount }.toFloat()
        val pieChartItems: MutableList<PieChartItem> = mutableListOf()

        var curStartAngle = startAngle
        for (i in categories.indices) {
            val rawSweep = (categories[i].totalAmount.toFloat() / total) * MAX_ANGLE
            val sweepAngle = (rawSweep - GAP_ANGLE).coerceAtLeast(MIN_ANGLE)

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
        writableState.update {
            it.copy(
                isThemeDark = !writableState.value.isThemeDark
            )
        }
        //TODO() в будущем поменять логику на shared preferences
    }

    private fun onFirstDayMonthClick() {
        if (!state.value.isOpenedFirstDayMonthDialog) {
            writableState.update {
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
            writableState.update {
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
        writableState.update {
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

        writableState.update {
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

    private fun onCurrencySettingsClick() {
        writableState.update {
            it.copy(
                isCurrencyMenuOpened = !it.isCurrencyMenuOpened
            )
        }
    }

    private fun onCurrencyChanged(newCurrency: Currency) {
        // TODO() реализовать пересчет баланса, списка категорий и общей суммы для новой валюты
        if (state.value.currency.code != newCurrency.code) {
            writableState.update {
                it.copy(
                    currency = newCurrency,
                    isCurrencyMenuOpened = false
                )
            }
        } else {
            onCurrencyMenuDismiss()
        }
    }

    private fun onCurrencyMenuDismiss() {
        writableState.update {
            it.copy(
                isCurrencyMenuOpened = false
            )
        }
    }

    private companion object {
        const val TWO_DECIMAL_PLACES = 2
        const val MIN_FIRST_DAY_MONTH = 1
        const val MAX_FIRST_DAY_MONTH = 28
        const val START_ANGLE = -90f
        const val GAP_ANGLE = 2f
        const val MIN_ANGLE = 0f
        const val MAX_ANGLE = 360f

    }
}
