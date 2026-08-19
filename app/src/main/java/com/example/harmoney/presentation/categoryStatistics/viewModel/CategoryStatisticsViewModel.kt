package com.example.harmoney.presentation.categoryStatistics.viewModel

import androidx.lifecycle.viewModelScope
import com.example.harmoney.base.BaseViewModel
import com.example.harmoney.core.session.SessionStateHolder
import com.example.harmoney.domain.models.CategoryStatistics
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.models.StatisticsPeriodType
import com.example.harmoney.domain.settings.period.api.useCase.FirstDayMonthInteractor
import com.example.harmoney.domain.settings.period.api.useCase.GetStatisticsPeriodsUseCase
import com.example.harmoney.domain.settings.theme.api.useCase.SetThemeUseCase
import com.example.harmoney.presentation.categoryStatistics.models.CategoryStatisticsAction
import com.example.harmoney.presentation.categoryStatistics.models.CategoryStatisticsEvent
import com.example.harmoney.presentation.categoryStatistics.models.CategoryStatisticsState
import com.example.harmoney.presentation.categoryStatistics.models.FirstDayMonthError
import com.example.harmoney.presentation.converters.CategoryStatisticsUiConverter
import com.example.harmoney.presentation.converters.NumbersFormatter
import com.example.harmoney.presentation.converters.StatisticsPeriodUiConverter
import com.example.harmoney.presentation.models.PieChartItem
import com.example.harmoney.presentation.test.TestDataSource
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update

@Suppress("detekt:LongParameterList", "detekt:TooManyFunctions")
class CategoryStatisticsViewModel(
    private val sessionSateHolder: SessionStateHolder,
    private val test: TestDataSource,
    private val categoryStatisticsUiConverter: CategoryStatisticsUiConverter,
    private val numbersFormatter: NumbersFormatter,
    private val statisticsPeriodUiConverter: StatisticsPeriodUiConverter,
    private val setThemeUseCase: SetThemeUseCase,
    private val firstDayMonthInteractor: FirstDayMonthInteractor,
    private val getStatisticsPeriodsUseCase: GetStatisticsPeriodsUseCase
) :
    BaseViewModel<CategoryStatisticsEvent, CategoryStatisticsAction, CategoryStatisticsState>(
        state = CategoryStatisticsState(
            selectedCategoryType = sessionSateHolder.state.value.categoryType,
            selectedTabIndex = sessionSateHolder.state.value.categoryType.ordinal,
        )
    ) {
    override val tag: String = CategoryStatisticsViewModel::class.java.simpleName ?: ""

    private val sessionFlow = sessionSateHolder.state
    private val periodsFlow = getStatisticsPeriodsUseCase()
    private val firstDayMonthFlow = firstDayMonthInteractor.getFirstDayMonth()
    //private val balanceFlow = getBalanceUseCase()
    //private val currencyFlow = getCurrencyUseCase()

    init {
        //TODO() считать валюту из shared preferences

        combine(
            sessionFlow,
            periodsFlow,
            firstDayMonthFlow,
            //balanceFlow,
            //currencyFlow
        ) { sessionData, periods, firstDayMonth ->//balance, currency

            val selectedPeriod = periods.first { it.type == sessionData.statisticsPeriodType }
            val categories = test.getCategoriesForStatistics(
                selectedPeriod, sessionData.categoryType
            )

            //Log.d("HarmAppTag", "catStatViewModel -> init -> combine -> selectedCategoryType =
            // ${sessionData.categoryType.name}, selectedPeriod = ${selectedPeriod.type.name},
            // firstDay = $firstDayMonth")//, statisticsSize = ${statistics.size}")

            val formattedCategories =
                categoryStatisticsUiConverter.map(categories, state.value.currency)

            val total = categories.sumOf { category -> category.totalAmount.minorUnits }

            writableState.update {
                it.copy(
                    selectedCategoryType = sessionData.categoryType,
                    selectedTabIndex = sessionData.categoryType.ordinal,
                    selectedStatisticsPeriod = statisticsPeriodUiConverter.map(selectedPeriod),
                    categories = formattedCategories,
                    pieChartCategories = getPieChartCategories(
                        categories = categories,
                        categoriesAmountString = formattedCategories
                            .map { category -> category.totalAmount }
                    ),
                    currentBalance = numbersFormatter.moneyToStringWithCurrency(
                        moneyMinorUnits = test.getBalance(),//balance
                        currency = state.value.currency,//currency,
                    ),
                    total = numbersFormatter.moneyToStringWithCurrency(
                        moneyMinorUnits = total,
                        currency = state.value.currency,//currency,it.currency,
                    ),
                    firstDayMonth = firstDayMonth,
                    firstDayMonthText = firstDayMonth.toString()
                )
            }
        }.launchIn(viewModelScope)
    }

    @Suppress("detekt:CyclomaticComplexMethod")
    override fun obtainEvent(event: CategoryStatisticsEvent) {
        when (event) {
            is CategoryStatisticsEvent.OnTabClick -> {
                onTabClick(newCategoryType = event.categoryType)
            }

            is CategoryStatisticsEvent.OnSettingsIconClick -> onNavigateToSettings()
            is CategoryStatisticsEvent.OnTransactionListIconClick -> {
                onNavigateToTransactionList(categoryId = null)
            }

            is CategoryStatisticsEvent.OnFloatingButtonClick -> onNavigateToTransaction()
            is CategoryStatisticsEvent.OnCategoryClick -> {
                onNavigateToTransactionList(event.categoryId)
            }

            is CategoryStatisticsEvent.OnStatisticsPeriodClick -> {
                onStatisticsPeriodClick(event.newPeriodType)
            }

            is CategoryStatisticsEvent.OnChangeTheme -> onThemeChanged(event.isThemeDark)

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

            sessionSateHolder.setCategoryType(newCategoryType)
        }
    }

    private fun onNavigateToSettings() {
        writableAction.tryEmit(CategoryStatisticsAction.NavigateToSettings)
    }

    private fun onNavigateToTransactionList(categoryId: Long?) {
        writableAction.tryEmit(
            CategoryStatisticsAction.NavigateToTransactionList(categoryId)
        )
    }

    private fun onNavigateToTransaction() {
        writableAction.tryEmit(CategoryStatisticsAction.NavigateToTransaction)
    }

    private fun onNavigateToCategoryList() {
        writableAction.tryEmit(CategoryStatisticsAction.NavigateToCategoryList)
    }

    private fun onStatisticsPeriodClick(newPeriod: StatisticsPeriodType) {
        if (newPeriod.id != state.value.selectedStatisticsPeriod.type.id) {

            sessionSateHolder.setPeriodType(newPeriod)
        }
    }

    private fun getPieChartCategories(
        categories: List<CategoryStatistics>,
        categoriesAmountString: List<String>,
    ): ImmutableList<PieChartItem> {
        val startAngle = START_ANGLE

        val total = categories.sumOf { it.totalAmount.minorUnits }.toFloat()
        val pieChartItems: MutableList<PieChartItem> = mutableListOf()

        var curStartAngle = startAngle
        for (i in categories.indices) {
            val rawSweep = (categories[i].totalAmount.minorUnits.toFloat() / total) * MAX_ANGLE
            val sweepAngle = (rawSweep - GAP_ANGLE).coerceAtLeast(MIN_ANGLE)

            pieChartItems.add(
                PieChartItem(
                    value = categoriesAmountString[i],
                    colorValue = categories[i].category.icon.color.background,
                    startAngle = curStartAngle,
                    sweepAngle = sweepAngle
                )
            )

            curStartAngle += rawSweep
        }

        return pieChartItems.toImmutableList()
    }

    private fun onThemeChanged(isThemeDark: Boolean) {
        runSafely(
            errorMessage = CHANGE_THEME_ERROR,
            block = {
                setThemeUseCase.execute(isThemeDark = isThemeDark)
            },
            onError = {
                writableAction.tryEmit(CategoryStatisticsAction.ShowChangeThemeError)
            }
        )
    }

    private fun onFirstDayMonthClick() {
        if (!state.value.isOpenedFirstDayMonthDialog) {
            writableState.update { it.copy(isOpenedFirstDayMonthDialog = true) }
        }
    }

    private fun onFirstDayMonthDialogConfirm() {
        val firstDay = state.value.firstDayMonthText.toIntOrNull()
        val isFirstDayCorrect = if (firstDay != null) {
            firstDay in MIN_FIRST_DAY_MONTH..MAX_FIRST_DAY_MONTH
        } else {
            false
        }

        if (isFirstDayCorrect) {
            runSafely(
                block = {
                    firstDayMonthInteractor.setFirstDayMonth(firstDay!!)
                },
                onError = {},
                errorMessage = SET_FIRST_DAY_MONTH_ERROR
            )
            writableState.update {
                it.copy(
                    firstDayMonthError = FirstDayMonthError.None,
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
                firstDayMonthError = FirstDayMonthError.None,
                isOpenedFirstDayMonthDialog = false
            )
        }
    }

    private fun onFirstDayMonthTextChanged(newText: String) {
        val firstDay = newText.toIntOrNull()
        val isFirstDayCorrect = if (firstDay != null) {
            firstDay in MIN_FIRST_DAY_MONTH..MAX_FIRST_DAY_MONTH
        } else {
            false
        }

        writableState.update {
            it.copy(
                firstDayMonthText = newText,
                firstDayMonthError = if (!isFirstDayCorrect) {
                    FirstDayMonthError.OutOfRange(
                        minDay = MIN_FIRST_DAY_MONTH,
                        maxDay = MAX_FIRST_DAY_MONTH
                    )
                } else {
                    FirstDayMonthError.None
                }
            )
        }
    }

    private fun onCurrencySettingsClick() {
        writableState.update {
            it.copy(isCurrencyMenuOpened = !it.isCurrencyMenuOpened)
        }
    }

    private fun onCurrencyChanged(newCurrency: Currency) {
        // TODO() реализовать пересчет баланса, списка категорий и общей суммы для новой валюты
        if (state.value.currency.code != newCurrency.code) {
            writableState.update {
                it.copy(currency = newCurrency, isCurrencyMenuOpened = false)
            }
        } else {
            onCurrencyMenuDismiss()
        }
    }

    private fun onCurrencyMenuDismiss() {
        writableState.update { it.copy(isCurrencyMenuOpened = false) }
    }

    private companion object {
        const val MIN_FIRST_DAY_MONTH = 1
        const val MAX_FIRST_DAY_MONTH = 28
        const val START_ANGLE = -90f
        const val GAP_ANGLE = 2f
        const val MIN_ANGLE = 0f
        const val MAX_ANGLE = 360f

        // Используются только для вывода логов
        const val CHANGE_THEME_ERROR = "Error switching the app theme"
        const val SET_FIRST_DAY_MONTH_ERROR = "Error setting first day month"
    }
}
