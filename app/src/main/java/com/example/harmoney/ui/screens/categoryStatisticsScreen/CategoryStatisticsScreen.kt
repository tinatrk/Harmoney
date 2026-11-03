package com.example.harmoney.ui.screens.categoryStatisticsScreen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.example.harmoney.R
import com.example.harmoney.core.uilibrary.buttons.HarmButton
import com.example.harmoney.core.uilibrary.drawers.HarmDrawer
import com.example.harmoney.core.uilibrary.snackbar.HarmSnackbar
import com.example.harmoney.core.uilibrary.topbars.HarmTopBar
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.StatisticsPeriodType
import com.example.harmoney.presentation.categoryStatistics.models.CategoryStatisticsAction
import com.example.harmoney.presentation.categoryStatistics.models.CategoryStatisticsContent
import com.example.harmoney.presentation.categoryStatistics.models.CategoryStatisticsEvent
import com.example.harmoney.presentation.categoryStatistics.models.CategoryStatisticsState
import com.example.harmoney.presentation.categoryStatistics.models.FirstDayMonthError
import com.example.harmoney.presentation.categoryStatistics.viewModel.CategoryStatisticsViewModel
import com.example.harmoney.presentation.models.ContentState
import com.example.harmoney.presentation.models.StatisticsPeriodUi
import com.example.harmoney.ui.components.ScreenWithCategoryTypeTabs
import com.example.harmoney.ui.other.PreviewData
import com.example.harmoney.ui.theme.HarmTheme
import kotlinx.collections.immutable.persistentListOf
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

                    is CategoryStatisticsAction.ShowError -> {
                        snackbarHostState.showSnackbar(
                            context.getString(act.error.resId)
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
                HarmTopBar.HarmMainTopBar(
                    title = {
                        CategoryStatisticsTopAppBar(
                            accountContentState = state.accountContent,
                            isAccountScopesMenuOpen = state.isAccountMenuOpened,
                            onEvent = onEvent
                        )
                    },
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
                    contentState = state.statisticsContent,
                    exchangeRateStatus = (state.accountContent as? ContentState.Success)
                        ?.content?.accountsExchangeRateStatus,
                    selectedPeriod = state.selectedStatisticsPeriod,
                    onEvent = onEvent,
                )
                CategoryStatisticsDialog(
                    firstDayMonthText = state.firstDayMonthText,
                    firstDayMonthError = state.firstDayMonthError,
                    isOpenedFirstDayMonthDialog = state.isOpenedFirstDayMonthDialog,
                    onEvent = onEvent
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CategoryStatisticsScreen_Empty_DarkPreview() {
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
                accountContent = ContentState.Success(PreviewData.getAccountContent()),
                statisticsContent = ContentState.Success(
                    CategoryStatisticsContent(
                        categories = persistentListOf(),
                        pieChartCategories = persistentListOf(),
                    )
                )
            ),
            drawerState = drawerState,
            snackbarState = snackbarHostState
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CategoryStatisticsScreen_Empty_LightPreview() {
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
                accountContent = ContentState.Success(PreviewData.getAccountContent()),
                statisticsContent = ContentState.Success(
                    CategoryStatisticsContent(
                        categories = persistentListOf(),
                        pieChartCategories = persistentListOf(),
                    )
                )
            ),
            drawerState = drawerState,
            snackbarState = snackbarHostState
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CategoryStatisticsScreen_DarkPreview() {
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
                accountContent = ContentState.Success(PreviewData.getAccountContent()),
                statisticsContent = ContentState.Success(
                    CategoryStatisticsContent(
                        categories = categories,
                        pieChartCategories = pieChartItems,
                        totalByCategoryType = total,
                    )
                ),
            ),
            drawerState = drawerState,
            snackbarState = snackbarHostState
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CategoryStatisticsScreen_LightPreview() {
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
                statisticsContent = ContentState.Success(
                    CategoryStatisticsContent(
                        categories = categories,
                        pieChartCategories = pieChartItems,
                        totalByCategoryType = total,
                    )
                ),
                accountContent = ContentState.Success(PreviewData.getAccountContent()),
            ),
            drawerState = drawerState,
            snackbarState = snackbarHostState
        )
    }
}

@Preview(showSystemUi = true, showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun CategoryStatisticsScreen_WithOpenAccountMenu_DarkPreview() {
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
                accountContent = ContentState.Success(PreviewData.getAccountContent()),
                statisticsContent = ContentState.Success(
                    CategoryStatisticsContent(
                        categories = categories,
                        pieChartCategories = pieChartItems,
                        totalByCategoryType = total,
                    )
                ),
                isAccountMenuOpened = true
            ),
            drawerState = drawerState,
            snackbarState = snackbarHostState
        )
    }
}

@Preview(showSystemUi = true, showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun CategoryStatisticsScreen_WithOpenAccountMenu_LightPreview() {
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
                statisticsContent = ContentState.Success(
                    CategoryStatisticsContent(
                        categories = categories,
                        pieChartCategories = pieChartItems,
                        totalByCategoryType = total,
                    )
                ),
                accountContent = ContentState.Success(PreviewData.getAccountContent()),
                isAccountMenuOpened = true,
            ),
            drawerState = drawerState,
            snackbarState = snackbarHostState
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CategoryStatisticsScreen_WithSettings_DarkPreview() {
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
private fun CategoryStatisticsScreen_WithSettings_LightPreview() {
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
private fun CategoryStatisticsScreen_WithFirstDayMonthDialog_DarkPreview() {
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
private fun CategoryStatisticsScreen_WithFirstDayMonthDialog_LightPreview() {
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
private fun CategoryStatisticsScreen_WithFirstDayMonthDialogError_DarkPreview() {
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
private fun CategoryStatisticsScreen_WithFirstDayMonthDialogError_LightPreview() {
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
