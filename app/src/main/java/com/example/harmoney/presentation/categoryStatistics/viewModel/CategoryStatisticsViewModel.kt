package com.example.harmoney.presentation.categoryStatistics.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.harmoney.core.util.AsyncResult
import com.example.harmoney.core.util.BaseViewModel
import com.example.harmoney.domain.account.models.AccountFailure
import com.example.harmoney.domain.account.models.AccountScopeWithBalance
import com.example.harmoney.domain.models.AccountScope
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.models.StatisticsPeriodType
import com.example.harmoney.domain.settings.theme.api.useCase.SetThemeUseCase
import com.example.harmoney.domain.statistics.models.StatisticsData
import com.example.harmoney.domain.statistics.models.StatisticsFailure
import com.example.harmoney.navigation.NavResultKeys
import com.example.harmoney.presentation.categoryStatistics.converters.CategoryStatisticsContentConverter
import com.example.harmoney.presentation.categoryStatistics.flows.CategoryStatisticsAccountFlowsFactory
import com.example.harmoney.presentation.categoryStatistics.flows.CategoryStatisticsSettingsFlows
import com.example.harmoney.presentation.categoryStatistics.flows.CategoryStatisticsStatisticsFlowsFactory
import com.example.harmoney.presentation.categoryStatistics.models.AccountContent
import com.example.harmoney.presentation.categoryStatistics.models.CategoryStatisticsAction
import com.example.harmoney.presentation.categoryStatistics.models.CategoryStatisticsEvent
import com.example.harmoney.presentation.categoryStatistics.models.CategoryStatisticsParamsFlows
import com.example.harmoney.presentation.categoryStatistics.models.CategoryStatisticsScreenError
import com.example.harmoney.presentation.categoryStatistics.models.CategoryStatisticsSettings
import com.example.harmoney.presentation.categoryStatistics.models.CategoryStatisticsState
import com.example.harmoney.presentation.categoryStatistics.models.FirstDayMonthError
import com.example.harmoney.presentation.converters.AccountContentConverter
import com.example.harmoney.presentation.converters.StatisticsPeriodUiConverter
import com.example.harmoney.presentation.models.AccountScopeUi
import com.example.harmoney.presentation.models.CategoryStatisticsContentError
import com.example.harmoney.presentation.models.ContentState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

@Suppress("detekt:LongParameterList", "detekt:TooManyFunctions")
class CategoryStatisticsViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val setThemeUseCase: SetThemeUseCase,

    private val categoryStatisticsSettingsFlows: CategoryStatisticsSettingsFlows,
    private val categoryStatisticsAccountFlowsFactory: CategoryStatisticsAccountFlowsFactory,
    private val categoryStatisticsStatisticsFlowsFactory: CategoryStatisticsStatisticsFlowsFactory,

    private val statisticsPeriodUiConverter: StatisticsPeriodUiConverter,
    private val categoryStatisticsContentConverter: CategoryStatisticsContentConverter,
    private val accountContentConverter: AccountContentConverter,
) :
    BaseViewModel<CategoryStatisticsEvent, CategoryStatisticsAction, CategoryStatisticsState>(
        state = CategoryStatisticsState(
            selectedCategoryType = categoryStatisticsSettingsFlows.selectedCategoryType,
            selectedTabIndex = categoryStatisticsSettingsFlows.selectedCategoryType.ordinal,
        )
    ) {

    private val accountFlows = categoryStatisticsAccountFlowsFactory.create(
        reportingCurrencyFlow = categoryStatisticsSettingsFlows.reportingCurrencyFlow,
        scope = viewModelScope
    )

    private val statisticsFlows = categoryStatisticsStatisticsFlowsFactory.create(
        CategoryStatisticsParamsFlows(
            selectedAccountScopeFlow = accountFlows.selectedAccountScopeFlow,
            selectedCategoryTypeFlow = categoryStatisticsSettingsFlows.selectedCategoryTypeFlow,
            selectedPeriodFlow = categoryStatisticsSettingsFlows.selectedPeriodFlow,
        ),
        categoryStatisticsSettingsFlows.reportingCurrencyFlow
    )

    private val navNewReportingCurrencyFlow: StateFlow<Long?> =
        savedStateHandle.getStateFlow(NavResultKeys.SELECTED_CURRENCY, null)

    init {
        observeAccountScopes()
        observeStatistics()
        observeScreenSettings()

        observeReportingCurrencyNavigationResult()
    }

    private fun observeAccountScopes() {
        combine(
            accountFlows.accountScopesWithBalanceFlow,
            accountFlows.selectedAccountScopeFlow
        ) { scopesResult, selectedScopeResult ->
            scopesResult to selectedScopeResult
        }.onEach { (scopesResult, selectedScopeResult) ->
            updateAccountScopesState(
                scopesResult = scopesResult,
                selectedScopeResult = selectedScopeResult
            )
        }.collectWithErrorHandling(
            errorMessage = ERROR_OBSERVE_ACCOUNTS,
            onError = {
                writableState.update {
                    it.copy(
                        accountContent = ContentState.Error(
                            CategoryStatisticsContentError.UPDATE_DATA_ERROR
                        )
                    )
                }
                writableAction.emit(
                    CategoryStatisticsAction
                        .ShowError(CategoryStatisticsScreenError.GET_ACCOUNTS_ERROR)
                )
            }
        )
    }

    private fun observeStatistics() {
        statisticsFlows.statisticsFlow.onEach { result ->
            updateStatisticsState(result)
        }.collectWithErrorHandling(
            errorMessage = ERROR_OBSERVE_STATISTICS,
            onError = {
                writableState.update {
                    it.copy(
                        statisticsContent = ContentState.Error(
                            CategoryStatisticsContentError.UPDATE_DATA_ERROR
                        )
                    )
                }
                writableState.update {
                    it.copy(
                        statisticsContent = ContentState.Error(
                            CategoryStatisticsContentError.UPDATE_DATA_ERROR
                        )
                    )
                }
            }
        )
    }

    private fun observeScreenSettings() {
        categoryStatisticsSettingsFlows.screenSettingsFlow.onEach { settings ->
            updateScreenSettings(settings)
        }.collectWithErrorHandling(
            errorMessage = ERROR_OBSERVING_SETTINGS,
            onError = {
                writableAction.emit(
                    CategoryStatisticsAction
                        .ShowError(CategoryStatisticsScreenError.GET_SETTINGS_ERROR)
                )
            }
        )
    }

    private fun updateAccountScopesState(
        scopesResult: AsyncResult<List<AccountScopeWithBalance>, StatisticsFailure>,
        selectedScopeResult: AsyncResult<AccountScope, AccountFailure>
    ) {
        when {
            scopesResult is AsyncResult.Error -> {
                writableState.update {
                    it.copy(
                        accountContent = ContentState.Error(
                            CategoryStatisticsContentError.UPDATE_DATA_ERROR
                        )
                    )
                }
                handleStatisticsError(scopesResult.error)
            }

            selectedScopeResult is AsyncResult.Error -> {
                writableState.update {
                    it.copy(
                        accountContent = ContentState.Error(
                            CategoryStatisticsContentError.UPDATE_DATA_ERROR
                        )
                    )
                }
                handleStatisticsError(
                    StatisticsFailure.Account(selectedScopeResult.error)
                )
            }

            scopesResult is AsyncResult.Loading || selectedScopeResult is AsyncResult.Loading -> {
                writableState.update { it.copy(accountContent = ContentState.Loading) }
            }

            scopesResult is AsyncResult.Success && selectedScopeResult is AsyncResult.Success -> {
                val content = accountContentConverter.map(
                    scopesResult.data, selectedScopeResult.data
                )

                writableState.update {
                    it.copy(accountContent = ContentState.Success(content))
                }
            }
        }
    }

    private fun updateStatisticsState(
        result: AsyncResult<StatisticsData, StatisticsFailure>
    ) {
        when (result) {
            is AsyncResult.Loading -> {
                writableState.update { it.copy(statisticsContent = ContentState.Loading) }
            }

            is AsyncResult.Error -> {
                writableState.update {
                    it.copy(
                        statisticsContent = ContentState
                            .Error(CategoryStatisticsContentError.UPDATE_DATA_ERROR)
                    )
                }
                handleStatisticsError(result.error)
            }

            is AsyncResult.Success -> {
                val content = categoryStatisticsContentConverter.map(result.data)

                writableState.update {
                    it.copy(
                        statisticsContent = ContentState.Success(content)
                    )
                }
            }
        }
    }

    private fun updateScreenSettings(settings: CategoryStatisticsSettings) {
        writableState.update {
            it.copy(
                selectedCategoryType = settings.selectedCategoryType,
                selectedTabIndex = settings.selectedCategoryType.ordinal,
                selectedStatisticsPeriod = statisticsPeriodUiConverter.map(settings.selectedPeriod),
                firstDayMonth = settings.firstDayMonth,
                firstDayMonthText = settings.firstDayMonth.toString(),
                firstDayMonthError = FirstDayMonthError.None,
                reportingCurrency = settings.reportingCurrency
            )
        }

    }

    private fun handleStatisticsError(failure: StatisticsFailure) {
        when (failure) {

            is StatisticsFailure.Account -> {
                writableAction.tryEmit(
                    CategoryStatisticsAction.ShowError(
                        CategoryStatisticsScreenError.GET_ACCOUNTS_ERROR
                    )
                )
            }

            is StatisticsFailure.Transaction -> {
                writableAction.tryEmit(
                    CategoryStatisticsAction.ShowError(
                        CategoryStatisticsScreenError.GET_STATISTICS_ERROR
                    )
                )
            }

            is StatisticsFailure.ExchangeRate -> {
                writableAction.tryEmit(
                    CategoryStatisticsAction.ShowError(
                        CategoryStatisticsScreenError.GET_STATISTICS_ERROR
                    )
                )
            }
        }
    }

    private fun observeReportingCurrencyNavigationResult() {
        navNewReportingCurrencyFlow
            .filterNotNull()
            .distinctUntilChanged()
            .onEach { currencyId ->
                val currency =
                    Currency.fromId(currencyId)

                if (currency != state.value.reportingCurrency) {

                    categoryStatisticsSettingsFlows.saveReportingCurrency(currency)
                }
            }.collectWithErrorHandling(
                errorMessage = ERROR_OBSERVE_REPORTING_CURRENCY_FROM_NAV_RESULT,
                onError = {
                    writableAction
                        .emit(
                            CategoryStatisticsAction
                                .ShowError(
                                    CategoryStatisticsScreenError
                                        .UPDATE_REPORTING_CURRENCY_ERROR
                                )
                        )
                }
            )
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

            is CategoryStatisticsEvent.OnAccountScopeMenuClick -> onAccountScopeMenuClick()
            is CategoryStatisticsEvent.OnAccountScopeMenuDismiss -> onAccountScopeMenuDismiss()
            is CategoryStatisticsEvent.OnAccountScopeChanged ->
                onAccountScopeChanged(event.newSelectedAccountScope)

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

            is CategoryStatisticsEvent.OnCategoryListClick -> {
                onNavigateToCategoryList()
            }
        }
    }

    private fun onTabClick(newCategoryType: CategoryType) {
        if (writableState.value.selectedCategoryType.id != newCategoryType.id) {
            categoryStatisticsSettingsFlows.saveSelectedCategoryType(newCategoryType)
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

    private fun onAccountScopeMenuClick() {
        writableState.update { it.copy(isAccountMenuOpened = !state.value.isAccountMenuOpened) }
    }

    private fun onAccountScopeMenuDismiss() {
        writableState.update { it.copy(isAccountMenuOpened = false) }
    }

    private fun onAccountScopeChanged(newScope: AccountScopeUi) {
        if (state.value.accountContent !is ContentState.Success<AccountContent>) {
            onAccountScopeMenuDismiss()
            return
        }
        val oldAccount =
            ((state.value.accountContent) as ContentState.Success<AccountContent>)
                .content.selectedAccountScope
        if (newScope.id != oldAccount?.id) {
            launchWithErrorHandling(
                errorMessage = ERROR_SET_SELECTED_ACCOUNT_SCOPE,
                onError = {
                    writableAction.emit(
                        CategoryStatisticsAction.ShowError(
                            CategoryStatisticsScreenError.SAVE_ACCOUNT_ERROR
                        )
                    )
                }
            ) {
                accountFlows.saveSelectedAccountScope(newScopeId = newScope.id)
            }
        }
        onAccountScopeMenuDismiss()
    }

    private fun onNavigateToTransaction() {
        writableAction.tryEmit(CategoryStatisticsAction.NavigateToTransaction)
    }

    private fun onNavigateToCategoryList() {
        writableAction.tryEmit(CategoryStatisticsAction.NavigateToCategoryList)
    }

    private fun onStatisticsPeriodClick(newPeriod: StatisticsPeriodType) {
        if (newPeriod.id != state.value.selectedStatisticsPeriod.type.id) {
            categoryStatisticsSettingsFlows.saveSelectedStatisticsPeriod(newPeriod)
        }
    }

    private fun onThemeChanged(isThemeDark: Boolean) {
        launchWithErrorHandling(
            errorMessage = ERROR_CHANGE_THEME,
            block = {
                setThemeUseCase.execute(isThemeDark = isThemeDark)
            },
            onError = {
                writableAction.tryEmit(
                    CategoryStatisticsAction.ShowError(
                        CategoryStatisticsScreenError.CHANGE_THEME_ERROR
                    )
                )
            }
        )
    }

    private fun onFirstDayMonthClick() {
        if (!state.value.isOpenedFirstDayMonthDialog) {
            writableState.update { it.copy(isOpenedFirstDayMonthDialog = true) }
        }
    }

    private fun onFirstDayMonthDialogConfirm() {
        val firstDay = state.value.firstDayMonthText.toValidFirstDayOfMonth()

        if (firstDay != null) {
            launchWithErrorHandling(
                block = {
                    categoryStatisticsSettingsFlows.updateFirstDayMonth(firstDay)
                },
                onError = {
                    writableAction.emit(
                        CategoryStatisticsAction
                            .ShowError(CategoryStatisticsScreenError.UPDATE_FIRST_DAY_MONTH_ERROR)
                    )
                },
                errorMessage = ERROR_SET_FIRST_DAY_MONTH
            )
            writableState.update {
                it.copy(
                    firstDayMonthError = FirstDayMonthError.None,
                    isOpenedFirstDayMonthDialog = false
                )
            }
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
        val firstDay = newText.toValidFirstDayOfMonth()

        writableState.update {
            it.copy(
                firstDayMonthText = newText,
                firstDayMonthError = if (firstDay == null) {
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

    private fun String.toValidFirstDayOfMonth(): Int? =
        toIntOrNull()?.takeIf {
            it in MIN_FIRST_DAY_MONTH..MAX_FIRST_DAY_MONTH
        }

    private fun onCurrencySettingsClick() {
        writableAction.tryEmit(
            CategoryStatisticsAction
                .NavigateToCurrency(state.value.reportingCurrency.id)
        )
    }

    private companion object {
        const val MIN_FIRST_DAY_MONTH = 1
        const val MAX_FIRST_DAY_MONTH = 28

        const val ERROR_CHANGE_THEME = "Error switching the app theme"
        const val ERROR_SET_FIRST_DAY_MONTH = "Error setting first day month"
        const val ERROR_SET_SELECTED_ACCOUNT_SCOPE = "Error saving selected account scope"
        const val ERROR_OBSERVE_ACCOUNTS = "Error observing account scopes"
        const val ERROR_OBSERVE_STATISTICS = "Error observing category statistics"
        const val ERROR_OBSERVING_SETTINGS = "Error observing settings"
        const val UNEXPECTED_RESOURCE_STATE_ERROR = "Unexpected Resource state"
        const val ERROR_OBSERVE_REPORTING_CURRENCY_FROM_NAV_RESULT =
            "Error observing reporting currency from navigation result"
    }
}
