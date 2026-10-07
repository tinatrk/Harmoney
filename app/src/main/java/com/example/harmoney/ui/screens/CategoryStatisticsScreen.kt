package com.example.harmoney.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.example.harmoney.R
import com.example.harmoney.core.uilibrary.buttons.HarmButton
import com.example.harmoney.core.uilibrary.cards.HarmCard
import com.example.harmoney.core.uilibrary.dialogs.HarmDialog
import com.example.harmoney.core.uilibrary.drawers.HarmDrawer
import com.example.harmoney.core.uilibrary.snackbar.HarmSnackbar
import com.example.harmoney.core.uilibrary.topbars.HarmTopBar
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.StatisticsPeriodType
import com.example.harmoney.presentation.categoryStatistics.models.CategoryStatisticsAction
import com.example.harmoney.presentation.categoryStatistics.models.CategoryStatisticsEvent
import com.example.harmoney.presentation.categoryStatistics.models.CategoryStatisticsState
import com.example.harmoney.presentation.categoryStatistics.models.FirstDayMonthError
import com.example.harmoney.presentation.categoryStatistics.viewModel.CategoryStatisticsViewModel
import com.example.harmoney.presentation.models.AccountScopeUi
import com.example.harmoney.presentation.models.StatisticsPeriodUi
import com.example.harmoney.ui.components.HarmMessage
import com.example.harmoney.ui.components.ScreenWithCategoryTypeTabs
import com.example.harmoney.ui.other.PreviewData
import com.example.harmoney.ui.theme.HarmTheme
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

@Composable
fun CategoryStatisticsScreen(
    viewModel: CategoryStatisticsViewModel,
    onNavigateToTransactionList: (categoryId: Long?) -> Unit,
    onNavigateToCreateTransaction: () -> Unit,
    onNavigateToCategoryList: () -> Unit,
    onNavigateToCurrency: (currencyId: Long) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.action
            .flowWithLifecycle(lifecycle, minActiveState = Lifecycle.State.STARTED)
            .collect { act ->
                when (act) {
                    is CategoryStatisticsAction.NavigateToTransactionList -> {
                        onNavigateToTransactionList(act.categoryId)
                    }

                    is CategoryStatisticsAction.NavigateToTransaction -> {
                        onNavigateToCreateTransaction()
                    }

                    is CategoryStatisticsAction.NavigateToSettings -> {
                        scope.launch {
                            if (drawerState.isClosed) {
                                drawerState.open()
                            } else {
                                drawerState.close()
                            }
                        }
                    }

                    is CategoryStatisticsAction.NavigateToCategoryList -> onNavigateToCategoryList()

                    is CategoryStatisticsAction.NavigateToCurrency -> {
                        onNavigateToCurrency(act.currencyId)
                    }

                    is CategoryStatisticsAction.ShowChangeThemeError -> {
                        snackbarHostState.showSnackbar(
                            context.getString(
                                R.string.toast_changed_theme_error
                            )
                        )
                    }

                    is CategoryStatisticsAction.ShowUpdateReportingCurrencyError -> {
                        snackbarHostState.showSnackbar(
                            context.getString(
                                R.string.toast_update_reporting_currency_error
                            )
                        )
                    }

                    is CategoryStatisticsAction.ShowGetSettingsError -> {
                        snackbarHostState.showSnackbar(
                            context.getString(
                                R.string.toast_load_settings_error
                            )
                        )
                    }

                    is CategoryStatisticsAction.ShowGetAccountsError -> {
                        snackbarHostState.showSnackbar(
                            context.getString(
                                R.string.toast_load_accounts_error
                            )
                        )
                    }

                    is CategoryStatisticsAction.ShowGetStatisticsError -> {
                        snackbarHostState.showSnackbar(
                            context.getString(
                                R.string.toast_load_statistics_error
                            )
                        )
                    }

                    is CategoryStatisticsAction.ShowUpdateFirstDayMonthError -> {
                        snackbarHostState.showSnackbar(
                            context.getString(
                                R.string.toast_update_first_day_month_error
                            )
                        )
                    }

                    null -> {}
                }
            }
    }

    CategoryStatisticsScreen(
        state = state,
        onEvent = viewModel::obtainEvent,
        drawerState = drawerState,
        snackbarState = snackbarHostState
    )
}

@Composable
fun CategoryStatisticsScreen(
    state: CategoryStatisticsState,
    drawerState: DrawerState,
    snackbarState: SnackbarHostState,
    onEvent: (CategoryStatisticsEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    HarmDrawer.HarmModalDrawer(
        title = stringResource(R.string.title_drawer_settings),
        drawerState = drawerState,
        drawerItems = {
            SettingsDrawerItems(
                isThemeDark = HarmTheme.colors.isDark,
                firstDayMonth = state.firstDayMonth.toString(),
                currentCurrencyCode = state.reportingCurrency.isoCode,
                onEvent = onEvent
            )
        }
    ) {
        Scaffold(
            modifier = modifier,
            topBar = {
                HarmTopBar.HarmCommonTopBar(
                    title = state.selectedAccountScope?.balance ?: "",
                    subtitle = state.selectedAccountScope?.name
                        ?: "",//stringResource(R.string.title_balance),
                    navigationIcon = {
                        HarmButton.HarmTopBarIconButton(
                            iconRes = R.drawable.ic_arrow_back_24px,
                            contentDescription = stringResource(R.string.ic_arrow_back_desc),
                            onClick = { onEvent(CategoryStatisticsEvent.OnSettingsIconClick) }
                        )
                    },
                    actionIcons = {
                        HarmButton.HarmTopBarIconButton(
                            iconRes = R.drawable.ic_list_24px,
                            contentDescription = stringResource(R.string.ic_list_desc),
                            onClick = {
                                onEvent(CategoryStatisticsEvent.OnTransactionListIconClick)
                            }
                        )
                    },
                    isTitleCenterAlignment = true
                )
            },
            containerColor = HarmTheme.colors.surface,
            floatingActionButton = {
                HarmButton.HarmFloatingActionButton(
                    iconRes = R.drawable.ic_add_24px,
                    contentDescription = stringResource(R.string.ic_add_transaction_desc),
                    onClick = { onEvent(CategoryStatisticsEvent.OnFloatingButtonClick) }
                )
            },
            snackbarHost = {
                SnackbarHost(hostState = snackbarState) { data ->
                    HarmSnackbar(snackbarData = data)
                }
            }
        ) { paddingValues ->
            ScreenWithCategoryTypeTabs(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues),
                tabs = CategoryType.entries.toImmutableList(),
                selectedTabIndex = state.selectedTabIndex,
                onTabClick = { categoryType ->
                    onEvent(CategoryStatisticsEvent.OnTabClick(categoryType))
                }
            ) {
                CategoryStatisticsContent(
                    state = state,
                    onEvent = onEvent,
                )
            }
        }
    }
}

@Composable
private fun SettingsDrawerItems(
    isThemeDark: Boolean,
    firstDayMonth: String,
    currentCurrencyCode: String,
    onEvent: (CategoryStatisticsEvent) -> Unit,
) {
    HarmDrawer.HarmDrawerItem(
        label = stringResource(R.string.title_drawer_item_theme),
        selected = false,
        onClick = { onEvent(CategoryStatisticsEvent.OnChangeTheme(!isThemeDark)) },
        badge = {
            HarmButton.HarmSwitch(
                isChecked = isThemeDark,
                onClick = { onEvent(CategoryStatisticsEvent.OnChangeTheme(!isThemeDark)) }
            )
        }
    )
    HarmDrawer.HarmDrawerItem(
        label = stringResource(R.string.title_drawer_item_first_day_month),
        selected = false,
        onClick = { onEvent(CategoryStatisticsEvent.OnFirstDayMonthClick) },
        badge = {
            Text(
                text = firstDayMonth,
                style = HarmTheme.typography.bodyLarge,
                color = HarmTheme.colors.onSurface
            )
        }
    )
    HarmDrawer.HarmDrawerItem(
        label = stringResource(R.string.title_drawer_currency),
        selected = false,
        onClick = { onEvent(CategoryStatisticsEvent.OnCurrencySettingsClick) },
        badge = {
            Text(
                text = currentCurrencyCode,
                style = HarmTheme.typography.bodyLarge,
                color = HarmTheme.colors.onSurface
            )
        }
    )
    HarmDrawer.HarmDrawerItem(
        label = stringResource(R.string.title_drawer_item_category_list),
        selected = false,
        onClick = { onEvent(CategoryStatisticsEvent.OnCategoryListClick) }
    )
}

@Composable
fun CategoryStatisticsContent(
    state: CategoryStatisticsState,
    onEvent: (CategoryStatisticsEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val supportText =
        when (val error = state.firstDayMonthError) {
            is FirstDayMonthError.None -> ""
            is FirstDayMonthError.OutOfRange -> {
                stringResource(
                    R.string.error_incorrect_first_day_month_pattern,
                    error.minDay,
                    error.maxDay
                )
            }
        }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HarmTheme.colors.surface)
    ) {
        HarmCard.HarmStatisticCard(
            periods = StatisticsPeriodType.entries.toImmutableList(),
            data = state.selectedStatisticsPeriod.date,
            pieChartItems = state.pieChartCategories,
            total = state.totalByCategoryType,
            selectedPeriod = state.selectedStatisticsPeriod.type,
            onPeriodClick = { newPeriod ->
                onEvent(CategoryStatisticsEvent.OnStatisticsPeriodClick(newPeriod))
            }
        )

        if (state.categories.isEmpty()) {
            HarmMessage(
                modifier = Modifier.fillMaxSize(),
                message = stringResource(R.string.placeholder_empty_transaction_list),
                imageRes = R.drawable.im_empty_screen
            )
        } else {
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(state.categories) { category ->
                    HarmCard.HarmCategoryCardSumTransactions(
                        modifier = Modifier.fillMaxWidth(),
                        category = category,
                        onCardClick = {
                            onEvent(
                                CategoryStatisticsEvent.OnCategoryClick(
                                    category.category.id
                                )
                            )
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
                item {
                    Spacer(modifier = Modifier.height(56.dp))
                }
            }
        }

        if (state.isOpenedFirstDayMonthDialog) {
            HarmDialog.HarmSetFirstDayMonthDialog(
                numberString = state.firstDayMonthText,
                onNumberChanged = { newText ->
                    onEvent(
                        CategoryStatisticsEvent.OnFirstDayMonthTextChanged(
                            newText
                        )
                    )
                },
                onConfirmation = { onEvent(CategoryStatisticsEvent.OnFirstDayMonthDialogConfirm) },
                onDismissRequest = {
                    onEvent(CategoryStatisticsEvent.OnFirstDayMonthDialogDismiss)
                },
                onTextFieldDoneAction = {
                    onEvent(CategoryStatisticsEvent.OnFirstDayMonthDialogConfirm)
                },
                isError = (state.firstDayMonthError != FirstDayMonthError.None),
                supportingText = supportText,
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CategoryStatisticsScreenDarkPreviewEmpty() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val snackbarHostState = remember { SnackbarHostState() }
    HarmTheme(darkTheme = true) {
        CategoryStatisticsScreen(
            onEvent = {},
            state = CategoryStatisticsState(
                selectedStatisticsPeriod = StatisticsPeriodUi(
                    type = StatisticsPeriodType.CURRENT_MONTH,
                    date = "01.03.2026 - 31.03.2026"
                ),
                selectedAccountScope = AccountScopeUi(
                    id = 0,
                    name = "Основная карта [RUB]",
                    balance = "0.00 ₽",
                    balanceOriginals = null,
                    isArchivedMark = false
                )
            ),
            drawerState = drawerState,
            snackbarState = snackbarHostState
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CategoryStatisticsScreenLightPreviewEmpty() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val snackbarHostState = remember { SnackbarHostState() }
    HarmTheme(darkTheme = false) {
        CategoryStatisticsScreen(
            onEvent = {},
            state = CategoryStatisticsState(
                selectedStatisticsPeriod = StatisticsPeriodUi(
                    type = StatisticsPeriodType.CURRENT_MONTH,
                    date = "01.03.2026 - 31.03.2026"
                ),
                selectedAccountScope = AccountScopeUi(
                    id = 0,
                    name = "Основная карта [RUB]",
                    balance = "0.00 ₽",
                    balanceOriginals = null,
                    isArchivedMark = false
                )
            ),
            drawerState = drawerState,
            snackbarState = snackbarHostState
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CategoryStatisticsScreenDarkPreview() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val snackbarHostState = remember { SnackbarHostState() }
    val categories = PreviewData.getExpensesCategoryStatistics()
    val total = "26 000 ₽"
    val pieChartItems = PreviewData.getExpensesPieChartCategories()

    HarmTheme(darkTheme = true) {
        CategoryStatisticsScreen(
            onEvent = {},
            state = CategoryStatisticsState(
                selectedStatisticsPeriod = StatisticsPeriodUi(
                    type = StatisticsPeriodType.CURRENT_MONTH,
                    date = "01.03.2026 - 31.03.2026"
                ),
                categories = categories,
                pieChartCategories = pieChartItems,
                totalByCategoryType = total,
                selectedAccountScope = AccountScopeUi(
                    id = 0,
                    name = "Основная карта [RUB]",
                    balance = "20 000 ₽",
                    balanceOriginals = null,
                    isArchivedMark = false
                )
            ),
            drawerState = drawerState,
            snackbarState = snackbarHostState
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CategoryStatisticsScreenLightPreview() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val snackbarHostState = remember { SnackbarHostState() }
    val categories = PreviewData.getExpensesCategoryStatistics()
    val total = "26 000 ₽"
    val pieChartItems = PreviewData.getExpensesPieChartCategories()

    HarmTheme(darkTheme = false) {
        CategoryStatisticsScreen(
            onEvent = {},
            state = CategoryStatisticsState(
                selectedStatisticsPeriod = StatisticsPeriodUi(
                    type = StatisticsPeriodType.CURRENT_MONTH,
                    date = "01.03.2026 - 31.03.2026"
                ),
                categories = categories,
                pieChartCategories = pieChartItems,
                totalByCategoryType = total,
                selectedAccountScope = AccountScopeUi(
                    id = 0,
                    name = "Основная карта [RUB]",
                    balance = "20 000 ₽",
                    balanceOriginals = null,
                    isArchivedMark = false
                )
            ),
            drawerState = drawerState,
            snackbarState = snackbarHostState
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CategoryStatisticsScreenDarkPreviewWithSettings() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
    val snackbarHostState = remember { SnackbarHostState() }
    HarmTheme(darkTheme = true) {
        CategoryStatisticsScreen(
            onEvent = {},
            state = CategoryStatisticsState(),
            drawerState = drawerState,
            snackbarState = snackbarHostState
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CategoryStatisticsScreenLightPreviewWithSettings() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
    val snackbarHostState = remember { SnackbarHostState() }
    HarmTheme(darkTheme = false) {
        CategoryStatisticsScreen(
            onEvent = {},
            state = CategoryStatisticsState(),
            drawerState = drawerState,
            snackbarState = snackbarHostState
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CategoryStatisticsScreenDarkPreviewWithFirstDayMonthDialog() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
    val snackbarHostState = remember { SnackbarHostState() }
    HarmTheme(darkTheme = true) {
        CategoryStatisticsScreen(
            onEvent = {},
            state = CategoryStatisticsState(isOpenedFirstDayMonthDialog = true),
            drawerState = drawerState,
            snackbarState = snackbarHostState
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CategoryStatisticsScreenLightPreviewWithFirstDayMonthDialog() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
    val snackbarHostState = remember { SnackbarHostState() }
    HarmTheme(darkTheme = false) {
        CategoryStatisticsScreen(
            onEvent = {},
            state = CategoryStatisticsState(isOpenedFirstDayMonthDialog = true),
            drawerState = drawerState,
            snackbarState = snackbarHostState
        )
    }
}

@Suppress("detekt:MagicNumber")
@Preview(showSystemUi = true)
@Composable
private fun CategoryStatisticsScreenDarkPreviewWithFirstDayMonthDialogError() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
    val snackbarHostState = remember { SnackbarHostState() }
    HarmTheme(darkTheme = true) {
        CategoryStatisticsScreen(
            onEvent = {},
            state = CategoryStatisticsState(
                isOpenedFirstDayMonthDialog = true,
                firstDayMonthText = "90",
                firstDayMonthError = FirstDayMonthError.OutOfRange(minDay = 1, maxDay = 28),
            ),
            drawerState = drawerState,
            snackbarState = snackbarHostState
        )
    }
}

@Suppress("detekt:MagicNumber")
@Preview(showSystemUi = true)
@Composable
private fun CategoryStatisticsScreenLightPreviewWithFirstDayMonthDialogError() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
    val snackbarHostState = remember { SnackbarHostState() }
    HarmTheme(darkTheme = false) {
        CategoryStatisticsScreen(
            onEvent = {},
            state = CategoryStatisticsState(
                isOpenedFirstDayMonthDialog = true,
                firstDayMonthText = "90",
                firstDayMonthError = FirstDayMonthError.OutOfRange(minDay = 1, maxDay = 28),
            ),
            drawerState = drawerState,
            snackbarState = snackbarHostState
        )
    }
}
