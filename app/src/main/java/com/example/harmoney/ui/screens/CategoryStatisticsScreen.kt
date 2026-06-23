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
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
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
import com.example.harmoney.core.uilibrary.menus.HarmMenu
import com.example.harmoney.core.uilibrary.topbars.HarmTopBar
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.models.StatisticsPeriod
import com.example.harmoney.presentation.categoryStatistics.models.CategoryStatisticsAction
import com.example.harmoney.presentation.categoryStatistics.models.CategoryStatisticsEvent
import com.example.harmoney.presentation.categoryStatistics.models.CategoryStatisticsState
import com.example.harmoney.presentation.categoryStatistics.models.FirstDayMonthError
import com.example.harmoney.presentation.categoryStatistics.viewModel.CategoryStatisticsViewModel
import com.example.harmoney.presentation.models.MenuOptions
import com.example.harmoney.presentation.sharedViewModel.SharedCategoryTypeViewModel
import com.example.harmoney.presentation.sharedViewModel.SharedStatisticsPeriodViewModel
import com.example.harmoney.ui.components.EmptyScreen
import com.example.harmoney.ui.components.ScreenWithCategoryTypeTabs
import com.example.harmoney.ui.other.PreviewData
import com.example.harmoney.ui.theme.HarmTheme
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

@Composable
fun CategoryStatisticsScreen(
    sharedCategoryTypeVM: SharedCategoryTypeViewModel,
    sharedStatisticsPeriodVM: SharedStatisticsPeriodViewModel,
    viewModel: CategoryStatisticsViewModel,
    onNavigateToTransactionList: (categoryId: Long?) -> Unit,
    onNavigateToCreateTransaction: () -> Unit,
    onNavigateToCategoryList: () -> Unit,
) {
    val categoryType by sharedCategoryTypeVM.selectedCategoryType.collectAsStateWithLifecycle()
    val statisticsPeriod by sharedStatisticsPeriodVM
        .selectedStatisticsPeriod.collectAsStateWithLifecycle()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    LaunchedEffect(categoryType) {
        viewModel.obtainEvent(
            CategoryStatisticsEvent.OnTabClick(categoryType = categoryType)
        )
    }

    LaunchedEffect(statisticsPeriod) {
        viewModel.obtainEvent(
            CategoryStatisticsEvent.OnStatisticsPeriodClick(statisticsPeriod)
        )
    }

    LaunchedEffect(Unit) {
        viewModel.action
            .flowWithLifecycle(lifecycle, minActiveState = Lifecycle.State.STARTED)
            .collect { act ->
                when (act) {
                    is CategoryStatisticsAction.NavigateToTransactionList -> {
                        onNavigateToTransactionList(act.categoryId)
                    }

                    CategoryStatisticsAction.NavigateToTransaction -> {
                        onNavigateToCreateTransaction()
                    }

                    CategoryStatisticsAction.NavigateToSettings -> {
                        scope.launch {
                            if (drawerState.isClosed) {
                                drawerState.open()
                            } else {
                                drawerState.close()
                            }
                        }
                    }

                    CategoryStatisticsAction.NavigateToCategoryList -> onNavigateToCategoryList()

                    else -> {}
                }
            }
    }

    CategoryStatisticsScreen(
        state = state,
        onEvent = viewModel::obtainEvent,
        onCategoryTypeChanged = sharedCategoryTypeVM::categoryTypeChanged,
        onStatisticsPeriodChanged = sharedStatisticsPeriodVM::statisticsPeriodChanged,
        drawerState = drawerState
    )
}

@Composable
fun CategoryStatisticsScreen(
    state: CategoryStatisticsState,
    drawerState: DrawerState,
    onEvent: (CategoryStatisticsEvent) -> Unit,
    onCategoryTypeChanged: (CategoryType) -> Unit,
    onStatisticsPeriodChanged: (StatisticsPeriod) -> Unit,
    modifier: Modifier = Modifier,
) {
    HarmDrawer.HarmModalDrawer(
        title = stringResource(R.string.title_drawer_settings),
        drawerState = drawerState,
        drawerItems = {
            SettingsDrawerItems(
                isThemeDark = state.isThemeDark,
                firstDayMonth = state.firstDayMonth.toString(),
                isCurrencyMenuOpened = state.isCurrencyMenuOpened,
                currentCurrencyCode = state.currency.code,
                onEvent = onEvent
            )
        }
    ) {
        Scaffold(
            modifier = modifier,
            topBar = {
                HarmTopBar.HarmCommonTopBar(
                    title = state.currentBalance,
                    subtitle = stringResource(R.string.title_balance),
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
            }
        ) { paddingValues ->
            ScreenWithCategoryTypeTabs(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues),
                tabs = CategoryType.entries.toImmutableList(),
                selectedTabIndex = state.selectedTabIndex,
                onTabClick = { categoryType -> onCategoryTypeChanged(categoryType) }
            ) {
                CategoryStatisticsContent(
                    state = state,
                    onEvent = onEvent,
                    onStatisticsPeriodChanged = onStatisticsPeriodChanged
                )
            }
        }
    }
}

@Composable
private fun SettingsDrawerItems(
    isThemeDark: Boolean,
    firstDayMonth: String,
    isCurrencyMenuOpened: Boolean,
    currentCurrencyCode: String,
    onEvent: (CategoryStatisticsEvent) -> Unit,
) {
    HarmDrawer.HarmDrawerItem(
        label = stringResource(R.string.title_drawer_item_theme),
        selected = false,
        onClick = { onEvent(CategoryStatisticsEvent.OnChangeTheme) },
        badge = {
            HarmButton.HarmSwitch(
                isChecked = isThemeDark,
                onClick = { onEvent(CategoryStatisticsEvent.OnChangeTheme) }
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
            HarmMenu.HarmDropdownMenu(
                expanded = isCurrencyMenuOpened,
                menuOptions = Currency.entries.sortedBy { it.code }.map { currency ->
                    MenuOptions(
                        text = currency.code,
                        expanded = currentCurrencyCode == currency.code
                    ) {
                        onEvent(CategoryStatisticsEvent.OnCurrencyChanged(currency))
                    }
                }.toImmutableList(),
                onDismissRequest = { onEvent(CategoryStatisticsEvent.OnCurrencyMenuDismiss) },
                isNeededHighlightSelectedOption = true
            ) {
                Text(
                    text = currentCurrencyCode,
                    style = HarmTheme.typography.bodyLarge,
                    color = HarmTheme.colors.onSurface
                )
            }
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
    onStatisticsPeriodChanged: (StatisticsPeriod) -> Unit,
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
            periods = StatisticsPeriod.entries.toImmutableList(),
            data = state.statisticsDate,
            pieChartItems = state.pieChartCategories,
            total = state.total,
            selectedPeriod = state.selectedStatisticsPeriod,
            onPeriodClick = { newPeriod -> onStatisticsPeriodChanged(newPeriod) }
        )

        if (state.categories.isEmpty()) {
            EmptyScreen(message = stringResource(R.string.placeholder_empty_transaction_list))
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
    HarmTheme(darkTheme = true) {
        CategoryStatisticsScreen(
            onEvent = {},
            onCategoryTypeChanged = {},
            onStatisticsPeriodChanged = {},
            state = CategoryStatisticsState(
                isThemeDark = true,
                statisticsDate = "01.03.2026 - 31.03.2026",
                currentBalance = "0.00 ₽"
            ),
            drawerState = drawerState
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CategoryStatisticsScreenLightPreviewEmpty() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    HarmTheme(darkTheme = false) {
        CategoryStatisticsScreen(
            onEvent = {},
            onCategoryTypeChanged = {},
            onStatisticsPeriodChanged = {},
            state = CategoryStatisticsState(
                isThemeDark = false,
                statisticsDate = "01.03.2026 - 31.03.2026",
                currentBalance = "0.00 ₽"
            ),
            drawerState = drawerState
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CategoryStatisticsScreenDarkPreview() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val categories = PreviewData.getExpensesCategoryStatistics()
    val total = "26 000 ₽"
    val pieChartItems = PreviewData.getExpensesPieChartCategories()

    HarmTheme(darkTheme = true) {
        CategoryStatisticsScreen(
            onEvent = {},
            onCategoryTypeChanged = {},
            onStatisticsPeriodChanged = {},
            state = CategoryStatisticsState(
                isThemeDark = true,
                statisticsDate = "01.03.2026 - 31.03.2026",
                categories = categories,
                pieChartCategories = pieChartItems,
                total = total,
                currentBalance = "20 000 ₽"
            ),
            drawerState = drawerState
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CategoryStatisticsScreenLightPreview() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val categories = PreviewData.getExpensesCategoryStatistics()
    val total = "26 000 ₽"
    val pieChartItems = PreviewData.getExpensesPieChartCategories()

    HarmTheme(darkTheme = false) {
        CategoryStatisticsScreen(
            onEvent = {},
            onCategoryTypeChanged = {},
            onStatisticsPeriodChanged = {},
            state = CategoryStatisticsState(
                isThemeDark = false,
                statisticsDate = "01.03.2026 - 31.03.2026",
                categories = categories,
                pieChartCategories = pieChartItems,
                total = total,
                currentBalance = "20 000 ₽"
            ),
            drawerState = drawerState
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CategoryStatisticsScreenDarkPreviewWithSettings() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
    HarmTheme(darkTheme = true) {
        CategoryStatisticsScreen(
            onEvent = {},
            onCategoryTypeChanged = {},
            onStatisticsPeriodChanged = {},
            state = CategoryStatisticsState(isThemeDark = true),
            drawerState = drawerState,
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CategoryStatisticsScreenLightPreviewWithSettings() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
    HarmTheme(darkTheme = false) {
        CategoryStatisticsScreen(
            onEvent = {},
            onCategoryTypeChanged = {},
            onStatisticsPeriodChanged = {},
            state = CategoryStatisticsState(isThemeDark = false),
            drawerState = drawerState
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CategoryStatisticsScreenDarkPreviewWithFirstDayMonthDialog() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
    HarmTheme(darkTheme = true) {
        CategoryStatisticsScreen(
            onEvent = {},
            onCategoryTypeChanged = {},
            onStatisticsPeriodChanged = {},
            state = CategoryStatisticsState(
                isThemeDark = true,
                isOpenedFirstDayMonthDialog = true
            ),
            drawerState = drawerState
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CategoryStatisticsScreenLightPreviewWithFirstDayMonthDialog() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
    HarmTheme(darkTheme = false) {
        CategoryStatisticsScreen(
            onEvent = {},
            onCategoryTypeChanged = {},
            onStatisticsPeriodChanged = {},
            state = CategoryStatisticsState(
                isThemeDark = false,
                isOpenedFirstDayMonthDialog = true
            ),
            drawerState = drawerState
        )
    }
}

@Suppress("detekt:MagicNumber")
@Preview(showSystemUi = true)
@Composable
private fun CategoryStatisticsScreenDarkPreviewWithFirstDayMonthDialogError() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
    HarmTheme(darkTheme = true) {
        CategoryStatisticsScreen(
            onEvent = {},
            onCategoryTypeChanged = {},
            onStatisticsPeriodChanged = {},
            state = CategoryStatisticsState(
                isThemeDark = true,
                isOpenedFirstDayMonthDialog = true,
                firstDayMonthText = "90",
                firstDayMonthError = FirstDayMonthError.OutOfRange(minDay = 1, maxDay = 28),
            ),
            drawerState = drawerState
        )
    }
}

@Suppress("detekt:MagicNumber")
@Preview(showSystemUi = true)
@Composable
private fun CategoryStatisticsScreenLightPreviewWithFirstDayMonthDialogError() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
    HarmTheme(darkTheme = false) {
        CategoryStatisticsScreen(
            onEvent = {},
            onCategoryTypeChanged = {},
            onStatisticsPeriodChanged = {},
            state = CategoryStatisticsState(
                isThemeDark = false,
                isOpenedFirstDayMonthDialog = true,
                firstDayMonthText = "90",
                firstDayMonthError = FirstDayMonthError.OutOfRange(minDay = 1, maxDay = 28),
            ),
            drawerState = drawerState
        )
    }
}
