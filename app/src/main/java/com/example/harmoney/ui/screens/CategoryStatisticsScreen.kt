package com.example.harmoney.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
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
import com.example.harmoney.domain.models.CategoryColors
import com.example.harmoney.domain.models.CategoryIcon
import com.example.harmoney.domain.models.CategoryIcons
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.presentation.categoryStatistics.models.CategoryStatisticsAction
import com.example.harmoney.presentation.categoryStatistics.models.CategoryStatisticsEvent
import com.example.harmoney.presentation.categoryStatistics.models.CategoryStatisticsState
import com.example.harmoney.presentation.categoryStatistics.models.FirstDayMonthError
import com.example.harmoney.presentation.categoryStatistics.viewModel.CategoryStatisticsViewModel
import com.example.harmoney.presentation.models.CategoryStatisticsUi
import com.example.harmoney.presentation.models.CategoryUi
import com.example.harmoney.presentation.models.MenuOptions
import com.example.harmoney.presentation.models.PieChartItem
import com.example.harmoney.ui.components.ScreenWithCategoryTypeTabs
import com.example.harmoney.ui.theme.HarmTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

@Composable
fun CategoryStatisticsScreen(
    viewModel: CategoryStatisticsViewModel,
    onNavigateToTransactionList: (categoryId: Long?) -> Unit,
    onNavigateToCreateTransaction: () -> Unit,
    onNavigateToCategoryList: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

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
        drawerState = drawerState
    )
}

@Composable
fun CategoryStatisticsScreen(
    state: CategoryStatisticsState,
    drawerState: DrawerState,
    onEvent: (CategoryStatisticsEvent) -> Unit,
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
                currentCurrency = state.currency.code,
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
                    navigationIconRes = R.drawable.ic_drawer_menu_24px,
                    navigationIconDesc = stringResource(R.string.ic_drawer_menu_desc),
                    onNavigationIconClick = {
                        onEvent(CategoryStatisticsEvent.OnSettingsIconClick)
                    },
                    actionIconRes = R.drawable.ic_list_24px,
                    actionIconDesc = stringResource(R.string.ic_list_desc),
                    onActionIconClick = {
                        onEvent(
                            CategoryStatisticsEvent
                                .OnTransactionListIconClick
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
                    onClick = {
                        onEvent(CategoryStatisticsEvent.OnFloatingButtonClick)
                    }
                )
            }
        ) { paddingValues ->
            ScreenWithCategoryTypeTabs(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues),
                tabs = state.categoryTypes,
                selectedTabIndex = state.selectedTabIndex,
                onTabClick = { categoryType ->
                    onEvent(
                        CategoryStatisticsEvent
                            .OnTabClick(categoryType)
                    )
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
    isCurrencyMenuOpened: Boolean,
    currentCurrency: String,
    onEvent: (CategoryStatisticsEvent) -> Unit,
) {
    HarmDrawer.HarmDrawerItem(
        label = stringResource(R.string.title_drawer_item_theme),
        selected = false,
        onClick = {
            onEvent(
                CategoryStatisticsEvent
                    .OnChangeTheme
            )
        },
        badge = {
            HarmButton.HarmSwitch(
                isChecked = isThemeDark,
                onClick = {
                    onEvent(
                        CategoryStatisticsEvent
                            .OnChangeTheme
                    )
                }
            )
        }
    )
    HarmDrawer.HarmDrawerItem(
        label = stringResource(R.string.title_drawer_item_first_day_month),
        selected = false,
        onClick = {
            onEvent(
                CategoryStatisticsEvent
                    .OnFirstDayMonthClick
            )
        },
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
        onClick = {
            onEvent(
                CategoryStatisticsEvent
                    .OnCurrencySettingsClick
            )
        },
        badge = {
            HarmMenu.HarmDropdownMenu(
                expanded = isCurrencyMenuOpened,
                menuOptions = Currency.entries.sortedBy { it.code }.map { currency ->
                    MenuOptions(
                        text = currency.code,
                    ) { onEvent(CategoryStatisticsEvent.OnCurrencyChanged(currency)) }
                }.toImmutableList(),
                onDismissRequest = { onEvent(CategoryStatisticsEvent.OnCurrencyMenuDismiss) }
            ) {
                Text(
                    text = currentCurrency,
                    style = HarmTheme.typography.bodyLarge,
                    color = HarmTheme.colors.onSurface
                )
            }
        }
    )
    HarmDrawer.HarmDrawerItem(
        label = stringResource(R.string.title_drawer_item_category_list),
        selected = false,
        onClick = {
            onEvent(
                CategoryStatisticsEvent
                    .OnCategoryListClick
            )
        }
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
            is FirstDayMonthError.IncorrectInput -> {
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
            periods = state.statisticsPeriods,
            data = state.statisticsDate,
            pieChartItems = state.pieChartCategories,
            total = state.total,
            selectedPeriodId = state.selectedStatisticsPeriod.id,
            onPeriodClick = { newPeriodId ->
                onEvent(CategoryStatisticsEvent.OnStatisticsPeriodClick(newPeriodId))
            }
        )

        if (state.categories.isEmpty()) {
            EmptyScreen()
        } else {
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
            ) {
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

@Composable
fun EmptyScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.im_empty_screen),
            contentDescription = stringResource(R.string.placeholder_empty_transaction_list)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.placeholder_empty_transaction_list),
            style = HarmTheme.typography.titleMediumSemiBold,
            color = HarmTheme.colors.onSurface,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(16.dp))
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CategoryStatisticsScreenDarkPreviewEmpty() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    HarmTheme(darkTheme = true) {
        CategoryStatisticsScreen(
            onEvent = {},
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
    val categories = getPreviewDataCategoryStatistics()
    val total = "26 000 ₽"
    val pieChartItems = getPreviewDataPieChartCategories()

    HarmTheme(darkTheme = true) {
        CategoryStatisticsScreen(
            onEvent = {},
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
    val categories = getPreviewDataCategoryStatistics()
    val total = "26 000 ₽"
    val pieChartItems = getPreviewDataPieChartCategories()

    HarmTheme(darkTheme = false) {
        CategoryStatisticsScreen(
            onEvent = {},
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
            state = CategoryStatisticsState(
                isThemeDark = false,
                isOpenedFirstDayMonthDialog = true
            ),
            drawerState = drawerState
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CategoryStatisticsScreenDarkPreviewWithFirstDayMonthDialogError() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
    HarmTheme(darkTheme = true) {
        CategoryStatisticsScreen(
            onEvent = {},
            state = CategoryStatisticsState(
                isThemeDark = true,
                isOpenedFirstDayMonthDialog = true,
                firstDayMonthText = "90",
                firstDayMonthError = FirstDayMonthError.IncorrectInput(1, 28),
            ),
            drawerState = drawerState
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CategoryStatisticsScreenLightPreviewWithFirstDayMonthDialogError() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
    HarmTheme(darkTheme = false) {
        CategoryStatisticsScreen(
            onEvent = {},
            state = CategoryStatisticsState(
                isThemeDark = false,
                isOpenedFirstDayMonthDialog = true,
                firstDayMonthText = "90",
                firstDayMonthError = FirstDayMonthError.IncorrectInput(1, 28),
            ),
            drawerState = drawerState
        )
    }
}

private fun getPreviewDataCategoryStatistics(): ImmutableList<CategoryStatisticsUi> {
    return persistentListOf(
        CategoryStatisticsUi(
            category = CategoryUi(
                id = 1,
                name = "Vacation",
                type = CategoryType.Expenses,
                icon = CategoryIcon(
                    icon = CategoryIcons.IC_VACATION_1,
                    color = CategoryColors.BLUE_T80
                ),
            ),
            totalAmount = "15 000 ₽",
            percentage = "57.7%",
        ),
        CategoryStatisticsUi(
            category = CategoryUi(
                id = 2,
                name = "Gifts",
                type = CategoryType.Expenses,
                icon = CategoryIcon(
                    icon = CategoryIcons.IC_GIFT,
                    color = CategoryColors.ORANGE_T70
                ),
            ),
            totalAmount = "7 500 ₽",
            percentage = "28.8%",
        ),
        CategoryStatisticsUi(
            category = CategoryUi(
                id = 3,
                name = "Products",
                type = CategoryType.Expenses,
                icon = CategoryIcon(
                    icon = CategoryIcons.IC_SHOP_CART,
                    color = CategoryColors.VIOLET_T68
                ),
            ),
            totalAmount = "3 500 ₽",
            percentage = "13.5%",
        ),
    )
}

private fun getPreviewDataPieChartCategories(
): ImmutableList<PieChartItem> {
    return persistentListOf(
        PieChartItem(
            value = "15 000 ₽",
            colorValue = CategoryColors.BLUE_T80.background,
            startAngle = -90f,
            sweepAngle = 205.69f,

            ),
        PieChartItem(
            value = "7 500 ₽",
            colorValue = CategoryColors.ORANGE_T70.background,
            startAngle = 117.69f,
            sweepAngle = 101.84f,

            ),
        PieChartItem(
            value = "3 500 ₽",
            colorValue = CategoryColors.VIOLET_T68.background,
            startAngle = 221.53f,
            sweepAngle = 46.46f,
        )
    )
}
