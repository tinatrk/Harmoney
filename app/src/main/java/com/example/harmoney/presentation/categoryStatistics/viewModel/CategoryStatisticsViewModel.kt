package com.example.harmoney.presentation.categoryStatistics.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.harmoney.core.session.SessionStateHolder
import com.example.harmoney.core.util.AsyncResult
import com.example.harmoney.core.util.BaseViewModel
import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.account.api.useCase.GetAccountListUseCase
import com.example.harmoney.domain.account.api.useCase.GetAccountScopesUseCase
import com.example.harmoney.domain.account.api.useCase.SelectedAccountScopeInteractor
import com.example.harmoney.domain.account.models.AccountFailure
import com.example.harmoney.domain.exchangeRate.api.useCase.ConvertCategoryStatisticsUseCase
import com.example.harmoney.domain.exchangeRate.api.useCase.ConvertTotalMoneyUseCase
import com.example.harmoney.domain.exchangeRate.models.ExchangeRateFailure
import com.example.harmoney.domain.models.Account
import com.example.harmoney.domain.models.AccountGroupType
import com.example.harmoney.domain.models.AccountScope
import com.example.harmoney.domain.models.CategoryStatistics
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.ConvertedCategoryStatistics
import com.example.harmoney.domain.models.ConvertedTotal
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.models.ExchangeRateStatus
import com.example.harmoney.domain.models.Money
import com.example.harmoney.domain.models.SortOption
import com.example.harmoney.domain.models.StatisticsPeriod
import com.example.harmoney.domain.models.StatisticsPeriodType
import com.example.harmoney.domain.settings.currency.api.useCase.ReportingCurrencyInteractor
import com.example.harmoney.domain.settings.period.api.useCase.FirstDayMonthInteractor
import com.example.harmoney.domain.settings.period.api.useCase.GetStatisticsPeriodsUseCase
import com.example.harmoney.domain.settings.theme.api.useCase.SetThemeUseCase
import com.example.harmoney.domain.statistics.api.useCase.GetCategoriesTotalUseCase
import com.example.harmoney.domain.statistics.api.useCase.GetCategoryStatisticsUseCase
import com.example.harmoney.domain.statistics.models.StatisticsFailure
import com.example.harmoney.navigation.NavResultKeys
import com.example.harmoney.presentation.categoryStatistics.models.CategoryStatisticsAction
import com.example.harmoney.presentation.categoryStatistics.models.CategoryStatisticsEvent
import com.example.harmoney.presentation.categoryStatistics.models.CategoryStatisticsState
import com.example.harmoney.presentation.categoryStatistics.models.FirstDayMonthError
import com.example.harmoney.presentation.converters.AccountScopeUiConverter
import com.example.harmoney.presentation.converters.CategoryStatisticsUiConverter
import com.example.harmoney.presentation.converters.ConvertedTotalFormatter
import com.example.harmoney.presentation.converters.ExchangeRateFormatter
import com.example.harmoney.presentation.converters.NumbersFormatter
import com.example.harmoney.presentation.converters.StatisticsPeriodUiConverter
import com.example.harmoney.presentation.models.PieChartItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update

@Suppress("detekt:LongParameterList", "detekt:TooManyFunctions")
class CategoryStatisticsViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val sessionSateHolder: SessionStateHolder,
    private val getStatisticsPeriodsUseCase: GetStatisticsPeriodsUseCase,
    private val setThemeUseCase: SetThemeUseCase,
    private val firstDayMonthInteractor: FirstDayMonthInteractor,
    private val reportingCurrencyInteractor: ReportingCurrencyInteractor,

    private val getAccountListUseCase: GetAccountListUseCase,
    private val selectedAccountScopeInteractor: SelectedAccountScopeInteractor,
    private val getAccountScopesUseCase: GetAccountScopesUseCase,

    private val getCategoryStatisticsUseCase: GetCategoryStatisticsUseCase,
    private val getCategoryTotalUseCase: GetCategoriesTotalUseCase,
    private val convertCategoryStatisticsUseCase: ConvertCategoryStatisticsUseCase,
    private val convertTotalMoneyUseCase: ConvertTotalMoneyUseCase,

    private val numbersFormatter: NumbersFormatter,
    private val accountScopeUiConverter: AccountScopeUiConverter,
    private val statisticsPeriodUiConverter: StatisticsPeriodUiConverter,
    private val categoryStatisticsUiConverter: CategoryStatisticsUiConverter,
    private val exchangeRateFormatter: ExchangeRateFormatter,
    private val convertedTotalFormatter: ConvertedTotalFormatter,
) :
    BaseViewModel<CategoryStatisticsEvent, CategoryStatisticsAction, CategoryStatisticsState>(
        state = CategoryStatisticsState(
            selectedCategoryType = sessionSateHolder.state.value.categoryType,
            selectedTabIndex = sessionSateHolder.state.value.categoryType.ordinal,
        )
    ) {

    // region flow data classes
    private sealed interface AccountScopeWithBalance {
        val scope: AccountScope

        data class Single(override val scope: AccountScope.Single, val balance: Money) :
            AccountScopeWithBalance

        data class Group(
            override val scope: AccountScope.Group,
            val balance: ConvertedTotal,
            val exchangeRateStatuses: Set<ExchangeRateStatus>
        ) :
            AccountScopeWithBalance
    }

    private data class StatisticsParams(
        val accountIds: List<Long>,
        val categoryType: CategoryType,
        val period: StatisticsPeriod,
    )

    private data class RawStatistics(
        val categories: List<CategoryStatistics>,
        val total: List<Money>,
    )

    private data class ConvertedStatistics(
        val categories: List<ConvertedCategoryStatistics>,
        val total: ConvertedTotal,
        val exchangeRateStatuses: Set<ExchangeRateStatus>
    )

    private data class ScreenSettings(
        val selectedCategoryType: CategoryType,
        val periods: List<StatisticsPeriod>,
        val selectedPeriod: StatisticsPeriod,
        val reportingCurrency: Currency,
        val firstDayMonth: Int,
    )

    private fun AccountScope.toIds(): List<Long> = accounts.map { it.id }
    // endregion

    // region flows
    private val sessionFlow = sessionSateHolder.state
    private val periodsFlow = getStatisticsPeriodsUseCase().distinctUntilChanged()
    private val selectedAccountScopeIdFlow =
        selectedAccountScopeInteractor.getSelectedScope().distinctUntilChanged()
    private val reportingCurrencyFlow = reportingCurrencyInteractor.getReportingCurrency()
        .distinctUntilChanged()
    private val firstDayMonthFlow =
        firstDayMonthInteractor.getFirstDayMonth().distinctUntilChanged()

    private val navNewReportingCurrencyFlow: StateFlow<Long?> =
        savedStateHandle.getStateFlow(NavResultKeys.SELECTED_CURRENCY, null)


    private val selectedPeriodFlow: Flow<StatisticsPeriod> =
        combine(sessionFlow, periodsFlow) { session, periods ->
            periods.first() { it.type == session.statisticsPeriodType }
        }.distinctUntilChanged()

    private val accountsFlow: SharedFlow<AsyncResult<List<Account>, AccountFailure>> =
        getAccountListUseCase.execute(SortOption.TIME_CREATED)
            .map { resource ->
                when (resource) {
                    is Resource.Success -> AsyncResult.Success(resource.data)
                    is Resource.Error -> AsyncResult.Error(resource.error)
                }
            }
            .onStart { emit(AsyncResult.Loading) }
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                replay = 1,
            )

    private val accountScopesFlow: Flow<AsyncResult<List<AccountScope>, AccountFailure>> =
        accountsFlow.map { accountsResult ->
            when (accountsResult) {
                is AsyncResult.Loading -> AsyncResult.Loading
                is AsyncResult.Error -> AsyncResult.Error(accountsResult.error)
                is AsyncResult.Success -> AsyncResult.Success(
                    getAccountScopesUseCase.execute(
                        accountsResult.data
                    )
                )
            }
        }.distinctUntilChanged()

    private val selectedAccountScopeFlow: Flow<AsyncResult<AccountScope, AccountFailure>> =
        combine(
            selectedAccountScopeIdFlow, accountScopesFlow
        ) { selectedScopeId, scopesResult ->
            when (scopesResult) {
                is AsyncResult.Loading -> AsyncResult.Loading
                is AsyncResult.Error -> AsyncResult.Error(scopesResult.error)
                is AsyncResult.Success -> {
                    val scopes = scopesResult.data
                    val selectedScope = scopes.firstOrNull { scope -> scope.id == selectedScopeId }
                        ?: scopes.firstOrNull { scope ->
                            scope.id == AccountGroupType.NOT_ARCHIVED.id
                        }
                        ?: scopes.first { scope -> scope.id == AccountGroupType.ALL.id }

                    AsyncResult.Success(selectedScope)
                }
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val accountScopesWithBalanceFlow
            : Flow<AsyncResult<List<AccountScopeWithBalance>, StatisticsFailure>> =
        combine(accountScopesFlow, reportingCurrencyFlow)
        { scopesResult, reportingCurrency ->
            scopesResult to reportingCurrency
        }.flatMapLatest { (scopesResult, reportingCurrency) ->
            when (scopesResult) {
                is AsyncResult.Loading -> flowOf(AsyncResult.Loading)
                is AsyncResult.Error -> flowOf(
                    AsyncResult.Error(
                        StatisticsFailure.Account(
                            scopesResult.error
                        )
                    )
                )

                is AsyncResult.Success -> flow {
                    emit(AsyncResult.Loading)
                    val result = mutableListOf<AccountScopeWithBalance>()

                    for (scope in scopesResult.data) {
                        val accountScopeWithBalanceResource =
                            getAccountScopeWithBalance(
                                scope = scope,
                                reportingCurrency = reportingCurrency
                            )

                        when (accountScopeWithBalanceResource) {
                            is Resource.Error -> {
                                emit(
                                    AsyncResult.Error(
                                        StatisticsFailure.ExchangeRate(
                                            accountScopeWithBalanceResource.error
                                        )
                                    )
                                )
                                return@flow
                            }

                            is Resource.Success -> {
                                result += accountScopeWithBalanceResource.data
                            }
                        }
                    }
                    emit(AsyncResult.Success(result))
                }
            }
        }

    private suspend fun getAccountScopeWithBalance(
        scope: AccountScope, reportingCurrency: Currency
    ): Resource<AccountScopeWithBalance, ExchangeRateFailure> {
        return when (scope) {
            is AccountScope.Single -> {
                val money = Money(
                    amount = scope.account.currentBalance,
                    currency = scope.account.currency
                )
                Resource.Success(AccountScopeWithBalance.Single(scope = scope, balance = money))
            }

            is AccountScope.Group -> {
                val balances = scope.accounts.map { account ->
                    Money(amount = account.currentBalance, currency = account.currency)
                }

                val convertedBalanceResource = convertTotalMoneyUseCase.execute(
                    moneyList = balances,
                    targetCurrency = reportingCurrency
                )
                when (convertedBalanceResource) {
                    is Resource.Error -> Resource.Error(convertedBalanceResource.error)
                    is Resource.Success -> Resource.Success(
                        AccountScopeWithBalance.Group(
                            scope = scope,
                            balance = convertedBalanceResource.data.total,
                            exchangeRateStatuses = convertedBalanceResource.data.exchangeRates
                        )
                    )
                }
            }
        }
    }

    private val statisticsParamsFlow: Flow<AsyncResult<StatisticsParams, AccountFailure>> =
        combine(
            selectedAccountScopeFlow,
            sessionFlow,
            selectedPeriodFlow
        ) { scopeResult, session, period ->
            when (scopeResult) {
                is AsyncResult.Loading -> AsyncResult.Loading
                is AsyncResult.Error -> AsyncResult.Error(scopeResult.error)
                is AsyncResult.Success ->
                    AsyncResult.Success(
                        StatisticsParams(
                            accountIds = scopeResult.data.toIds(),
                            categoryType = session.categoryType,
                            period = period
                        )
                    )

            }
        }.distinctUntilChanged()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val rawStatisticsFlow: Flow<AsyncResult<RawStatistics, StatisticsFailure>> =
        statisticsParamsFlow.flatMapLatest { paramsResult ->
            when (paramsResult) {
                is AsyncResult.Loading -> flowOf(AsyncResult.Loading)
                is AsyncResult.Error -> {
                    flowOf(
                        AsyncResult.Error(
                            StatisticsFailure
                                .Account(paramsResult.error)
                        )
                    )
                }

                is AsyncResult.Success -> {
                    val params = paramsResult.data

                    combine(
                        getCategoryStatisticsUseCase(
                            accountIds = params.accountIds,
                            categoryType = params.categoryType,
                            period = params.period
                        ),
                        getCategoryTotalUseCase.execute(
                            accountIds = params.accountIds,
                            categoryType = params.categoryType,
                            period = params.period
                        )
                    ) { categoriesResource, totalResource ->
                        when {
                            categoriesResource is Resource.Error -> AsyncResult.Error(
                                StatisticsFailure.Transaction(
                                    categoriesResource.error
                                )
                            )

                            totalResource is Resource.Error -> AsyncResult.Error(
                                StatisticsFailure.Transaction(
                                    totalResource.error
                                )
                            )

                            categoriesResource is Resource.Success
                                    && totalResource is Resource.Success ->
                                AsyncResult.Success(
                                    RawStatistics(
                                        categories = categoriesResource.data,
                                        total = totalResource.data
                                    )
                                )


                            else -> error(UNEXPECTED_RESOURCE_STATE_ERROR)
                        }
                    }.onStart { emit(AsyncResult.Loading) }
                }
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val convertedStatisticsFlow: Flow<AsyncResult<ConvertedStatistics, StatisticsFailure>> =
        combine(rawStatisticsFlow, reportingCurrencyFlow) { rawResult, reportingCurrency ->
            rawResult to reportingCurrency
        }.flatMapLatest { (rawResult, reportingCurrency) ->
            when (rawResult) {
                is AsyncResult.Loading -> flowOf(AsyncResult.Loading)
                is AsyncResult.Error -> flowOf(AsyncResult.Error(rawResult.error))
                is AsyncResult.Success -> flow {
                    emit(AsyncResult.Loading)

                    val raw = rawResult.data
                    val categoriesResource = convertCategoryStatisticsUseCase.execute(
                        categories = raw.categories,
                        targetCurrency = reportingCurrency,
                    )

                    if (categoriesResource is Resource.Error) {
                        emit(
                            AsyncResult.Error(
                                StatisticsFailure
                                    .ExchangeRate(categoriesResource.error)
                            )
                        )
                        return@flow
                    }

                    val totalResource = convertTotalMoneyUseCase.execute(
                        moneyList = raw.total,
                        targetCurrency = reportingCurrency
                    )

                    if (totalResource is Resource.Error) {
                        emit(
                            AsyncResult.Error(
                                StatisticsFailure
                                    .ExchangeRate(totalResource.error)
                            )
                        )
                        return@flow
                    }

                    when {
                        categoriesResource is Resource.Success
                                && totalResource is Resource.Success -> {
                            emit(
                                AsyncResult.Success(
                                    ConvertedStatistics(
                                        categories = categoriesResource.data.categoryStatistics,
                                        total = totalResource.data.total,
                                        exchangeRateStatuses = categoriesResource.data.exchangeRates
                                                + totalResource.data.exchangeRates
                                    )
                                )
                            )
                        }

                        else -> emit(error(UNEXPECTED_RESOURCE_STATE_ERROR))
                    }
                }
            }
        }

    private val screenSettingsFlow: Flow<ScreenSettings> =
        combine(
            sessionFlow,
            periodsFlow,
            selectedPeriodFlow,
            reportingCurrencyFlow,
            firstDayMonthFlow,
        ) { session, periods, selectedPeriod, reportingCurrency, firstDayMonth ->
            ScreenSettings(
                selectedCategoryType = session.categoryType,
                periods = periods,
                selectedPeriod = selectedPeriod,
                reportingCurrency = reportingCurrency,
                firstDayMonth = firstDayMonth
            )
        }.distinctUntilChanged()
    // endregion

    init {
        observeAccountScopes()
        observeStatistics()
        observeScreenSettings()

        observeReportingCurrencyNavigationResult()
    }

    private fun observeAccountScopes() {
        combine(
            accountScopesWithBalanceFlow,
            selectedAccountScopeFlow
        ) { scopesResult, selectedScopeResult ->
            scopesResult to selectedScopeResult
        }.onEach { (scopesResult, selectedScopeResult) ->
            updateAccountScopesState(
                scopesResult = scopesResult,
                selectedScopeResult = selectedScopeResult
            )
        }.collectWithErrorHandling(
            errorMessage = ERROR_OBSERVE_ACCOUNTS,
            onError = { writableAction.emit(CategoryStatisticsAction.ShowGetAccountsError) }
        )
    }

    private fun observeStatistics() {
        convertedStatisticsFlow.onEach { result ->
            updateStatisticsState(result)
        }.collectWithErrorHandling(
            errorMessage = ERROR_OBSERVE_STATISTICS,
            onError = { writableState.update { it.copy(isStatisticsError = true) } }
        )
    }

    private fun observeScreenSettings() {
        screenSettingsFlow.onEach { settings ->
            updateScreenSettings(settings)
        }.collectWithErrorHandling(
            errorMessage = ERROR_OBSERVING_SETTINGS,
            onError = { writableAction.emit(CategoryStatisticsAction.ShowGetSettingsError) }
        )
    }

    private fun updateAccountScopesState(
        scopesResult: AsyncResult<List<AccountScopeWithBalance>, StatisticsFailure>,
        selectedScopeResult: AsyncResult<AccountScope, AccountFailure>
    ) {
        when (scopesResult) {
            is AsyncResult.Loading -> {
                writableState.update { it.copy(isAccountsLoading = true) }
            }

            is AsyncResult.Error -> {
                writableState.update { it.copy(isAccountsLoading = false) }
                handleStatisticsError(scopesResult.error)
            }

            is AsyncResult.Success -> {
                val selectedScope = when (selectedScopeResult) {
                    is AsyncResult.Success -> {
                        val selected = scopesResult.data.find {
                            it.scope.id == selectedScopeResult.data.id
                        }
                        when (selected) {
                            is AccountScopeWithBalance.Single ->
                                accountScopeUiConverter.singleMap(selected.scope, selected.balance)

                            is AccountScopeWithBalance.Group ->
                                accountScopeUiConverter.groupMap(selected.scope, selected.balance)

                            null -> null
                        }
                    }

                    else -> null
                }

                val exchangeRateStatuses: MutableSet<ExchangeRateStatus> = mutableSetOf()

                val scopesUi = scopesResult.data.map { item ->
                    when (item) {
                        is AccountScopeWithBalance.Single -> {
                            accountScopeUiConverter.singleMap(item.scope, item.balance)
                        }

                        is AccountScopeWithBalance.Group -> {
                            exchangeRateStatuses.addAll(item.exchangeRateStatuses)
                            accountScopeUiConverter.groupMap(item.scope, item.balance)
                        }
                    }
                }.toImmutableList()

                val exchangeRateStatus = exchangeRateFormatter.map(exchangeRateStatuses)

                writableState.update {
                    it.copy(
                        isAccountsLoading = false,
                        accountScopes = scopesUi,
                        selectedAccountScope = selectedScope,
                        accountsExchangeRateStatus = exchangeRateStatus
                    )
                }
            }
        }
    }

    private fun updateStatisticsState(
        result: AsyncResult<ConvertedStatistics, StatisticsFailure>
    ) {
        when (result) {
            is AsyncResult.Loading -> {
                writableState.update { it.copy(isStatisticsLoading = true) }
            }

            is AsyncResult.Error -> {
                writableState.update {
                    it.copy(isStatisticsLoading = false, isStatisticsError = true)
                }
                handleStatisticsError(result.error)
            }

            is AsyncResult.Success -> {
                val statistics = result.data
                val formattedCategories = categoryStatisticsUiConverter.map(
                    statistics.categories,
                    statistics.total
                )

                writableState.update {
                    it.copy(
                        isStatisticsLoading = false,
                        isStatisticsError = false,
                        categories = formattedCategories,
                        pieChartCategories = getPieChartCategories(
                            categories = statistics.categories,
                            categoriesAmountString = formattedCategories
                                .map { category -> category.totalAmount }
                        ),
                        totalByCategoryType = numbersFormatter
                            .moneyToStringWithCurrency(statistics.total.total),
                        totalOriginalsByCategoryType = convertedTotalFormatter.getOriginalsStr(
                            statistics.total
                        )
                    )
                }
            }
        }
    }

    private fun updateScreenSettings(settings: ScreenSettings) {
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
                writableAction.tryEmit(CategoryStatisticsAction.ShowGetAccountsError)
            }

            is StatisticsFailure.Transaction -> {
                writableAction.tryEmit(CategoryStatisticsAction.ShowGetStatisticsError)
            }

            is StatisticsFailure.ExchangeRate -> {
                writableAction.tryEmit(CategoryStatisticsAction.ShowGetStatisticsError)
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

                    reportingCurrencyInteractor.saveReportingCurrency(currency)
                }
            }.collectWithErrorHandling(
                errorMessage = ERROR_OBSERVE_REPORTING_CURRENCY_FROM_NAV_RESULT,
                onError = {
                    writableAction
                        .emit(CategoryStatisticsAction.ShowUpdateReportingCurrencyError)
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
        categories: List<ConvertedCategoryStatistics>,
        categoriesAmountString: List<String>,
    ): ImmutableList<PieChartItem> {
        val startAngle = START_ANGLE

        val total = categories.sumOf { it.total.total.amount.minorUnits }.toFloat()
        val pieChartItems: MutableList<PieChartItem> = mutableListOf()

        var curStartAngle = startAngle
        for (i in categories.indices) {
            val rawSweep =
                (categories[i].total.total.amount.minorUnits.toFloat() / total) * MAX_ANGLE
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
        launchWithErrorHandling(
            errorMessage = ERROR_CHANGE_THEME,
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
            launchWithErrorHandling(
                block = {
                    firstDayMonthInteractor.setFirstDayMonth(firstDay!!)
                },
                onError = {
                    writableAction.emit(
                        CategoryStatisticsAction.ShowUpdateFirstDayMonthError
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
        writableAction.tryEmit(
            CategoryStatisticsAction
                .NavigateToCurrency(state.value.reportingCurrency.id)
        )
    }

    private companion object {
        const val MIN_FIRST_DAY_MONTH = 1
        const val MAX_FIRST_DAY_MONTH = 28
        const val START_ANGLE = -90f
        const val GAP_ANGLE = 2f
        const val MIN_ANGLE = 0f
        const val MAX_ANGLE = 360f

        const val ERROR_CHANGE_THEME = "Error switching the app theme"
        const val ERROR_SET_FIRST_DAY_MONTH = "Error setting first day month"
        const val ERROR_OBSERVE_ACCOUNTS = "Error observing account scopes"
        const val ERROR_OBSERVE_STATISTICS = "Error observing category statistics"
        const val ERROR_OBSERVING_SETTINGS = "Error observing settings"
        const val UNEXPECTED_RESOURCE_STATE_ERROR = "Unexpected Resource state"
        const val ERROR_OBSERVE_REPORTING_CURRENCY_FROM_NAV_RESULT =
            "Error observing reporting currency from navigation result"
    }
}
