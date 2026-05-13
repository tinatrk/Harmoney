package com.example.harmoney.presentation.categoryStatistics.viewModel

import com.example.harmoney.base.BaseViewModel
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
    private val numbersFormatter: NumbersFormatter
) :
    BaseViewModel<CategoryStatisticsEvent, CategoryStatisticsAction, CategoryStatisticsState>(
        state = CategoryStatisticsState()
    ) {
    override val tag: String = CategoryStatisticsViewModel::class.java.simpleName ?: ""

    init {
        // считать тему из shared preferences
        _state.update {
            val categories =
                test.getCategoriesForStatistics(
                    it.selectedStatisticsPeriod,
                    it.categoryType
                )

            val total = categories.sumOf { category -> category.totalAmount }

            it.copy(
                statisticsDate = test.getStatisticsDate(it.selectedStatisticsPeriod),//"25.03.2026 - 24.04.2026",
                categories = categoryConverter.map(categories, it.currency),
                pieChartCategories = getPieChartCategories(categories, it.currency),
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

            is CategoryStatisticsEvent.OnChangeTheme -> {
                // в будущем поменять логику на shared preferences
                _state.update {
                    it.copy(
                        isThemeDark = !_state.value.isThemeDark
                    )
                }
            }

            is CategoryStatisticsEvent.OnFirstDayMonthClick -> {/* работа с диалоговым окном */
            }

            is CategoryStatisticsEvent.OnCategoryListClick -> {
                onNavigateToCategoryList()
            }

            is CategoryStatisticsEvent.OnStatisticsPeriodClick -> {
                onStatisticsPeriodClick(event.newPeriodId)
            }
        }
    }

    private fun onNavigateToTransactionList(categoryId: Long?) {
        _action.tryEmit(CategoryStatisticsAction.NavigateToTransactionList(categoryId))
    }

    private fun onNavigateToTransaction() {
        _action.tryEmit(CategoryStatisticsAction.NavigateToTransaction)
    }

    private fun onNavigateToSettings() {
        _action.tryEmit(CategoryStatisticsAction.NavigateToSettings)
    }

    private fun onNavigateToCategoryList() {
        _action.tryEmit(CategoryStatisticsAction.NavigateToCategoryList)
    }

    private fun onTabClick(newCategoryType: CategoryType) {
        if (_state.value.categoryType.id != newCategoryType.id) {
            _state.update {
                val categories =
                    test.getCategoriesForStatistics(
                        statisticPeriod = it.selectedStatisticsPeriod,
                        categoryType = newCategoryType
                    )

                val total = categories.sumOf { category -> category.totalAmount }

                it.copy(
                    categoryType = newCategoryType,
                    selectedTabIndex = newCategoryType.ordinal,
                    categories = categoryConverter.map(categories, it.currency),
                    pieChartCategories = getPieChartCategories(categories, it.currency),
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

    private fun onStatisticsPeriodClick(newPeriodId: Long) {
        _state.update {
            val newPeriod = StatisticPeriod.fromId(newPeriodId)
            val categories = test.getCategoriesForStatistics(
                statisticPeriod = newPeriod,
                categoryType = it.categoryType
            )
            val total = categories.sumOf { category -> category.totalAmount }

            it.copy(
                categories = categoryConverter.map(categories, it.currency),
                pieChartCategories = getPieChartCategories(categories, it.currency),
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
        currency: Currency
    ): List<PieChartItem> {
        val startAngle = -90f
        val gapAngle = 2f
        val minAngle = 0f
        val maxAngle = 360f

        val total = categories.sumOf { it.totalAmount }.toFloat()

        var curStartAngle = startAngle
        val pieChartItems = categories.map { category ->
            val rawSweep = (category.totalAmount.toFloat() / total) * maxAngle
            val sweepAngle = (rawSweep - gapAngle).coerceAtLeast(minAngle)
            PieChartItem( // здесь происходит дублирование преобразования с categories
                value = numbersFormatter.toStringWithCurrency(
                    number = category.totalAmount,
                    decimalPlaces = TWO_DECIMAL_PLACES,
                    currency = currency,
                    isNeededThousandSeparator = true
                ),
                colorValue = category.category.icon.colors.background,
                startAngle = curStartAngle,
                sweepAngle = sweepAngle
            ).also { curStartAngle += rawSweep }
        }

        return pieChartItems
    }

    // логика переключения валюты

    private companion object {
        const val TWO_DECIMAL_PLACES = 2
        const val ZERO_DECIMAL_PLACES = 0
    }
}
