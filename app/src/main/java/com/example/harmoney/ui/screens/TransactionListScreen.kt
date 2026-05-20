package com.example.harmoney.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.example.harmoney.R
import com.example.harmoney.core.uilibrary.buttons.HarmButton
import com.example.harmoney.core.uilibrary.cards.HarmCard
import com.example.harmoney.core.uilibrary.date.HarmDate
import com.example.harmoney.core.uilibrary.menus.HarmMenu
import com.example.harmoney.core.uilibrary.topbars.HarmTopBar
import com.example.harmoney.domain.models.CategoryColors
import com.example.harmoney.domain.models.CategoryIcon
import com.example.harmoney.domain.models.CategoryIcons
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.StatisticsPeriod
import com.example.harmoney.presentation.models.CategoryUi
import com.example.harmoney.presentation.models.MenuOptions
import com.example.harmoney.presentation.models.OneDayTransactionsUi
import com.example.harmoney.presentation.models.TransactionUi
import com.example.harmoney.presentation.models.TransactionsFilterUi
import com.example.harmoney.presentation.sharedViewModel.SharedCategoryTypeViewModel
import com.example.harmoney.presentation.sharedViewModel.SharedStatisticsPeriodViewModel
import com.example.harmoney.presentation.transactionList.models.TransactionListAction
import com.example.harmoney.presentation.transactionList.models.TransactionListEvent
import com.example.harmoney.presentation.transactionList.models.TransactionListState
import com.example.harmoney.presentation.transactionList.viewModel.TransactionListViewModel
import com.example.harmoney.ui.components.EmptyScreen
import com.example.harmoney.ui.components.ScreenWithCategoryTypeTabs
import com.example.harmoney.ui.theme.HarmTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Composable
fun TransactionListScreen(
    sharedCategoryTypeVM: SharedCategoryTypeViewModel,
    sharedStatisticsPeriodVM: SharedStatisticsPeriodViewModel,
    viewModel: TransactionListViewModel,
    onBackClick: () -> Unit,
    onNavigateToCreateTransaction: (categoryId: Long?) -> Unit,
    onNavigateToOpenTransaction: (transactionId: Long?) -> Unit,
) {
    val categoryType by sharedCategoryTypeVM.selectedCategoryType.collectAsStateWithLifecycle()
    val statisticsPeriod by sharedStatisticsPeriodVM
        .selectedStatisticsPeriod.collectAsStateWithLifecycle()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    LaunchedEffect(categoryType) {
        viewModel.obtainEvent(TransactionListEvent.OnTabClick(categoryType = categoryType))
    }

    LaunchedEffect(statisticsPeriod) {
        viewModel.obtainEvent(
            TransactionListEvent
                .OnStatisticsPeriodClick(statisticsPeriod)
        )
    }

    LaunchedEffect(Unit) {
        viewModel.action
            .flowWithLifecycle(lifecycle, minActiveState = Lifecycle.State.STARTED)
            .collect { act ->
                when (act) {
                    is TransactionListAction.NavigateToCreatingTransaction -> {
                        onNavigateToCreateTransaction(act.categoryId)
                    }

                    is TransactionListAction.NavigateToOpeningTransaction -> {
                        onNavigateToOpenTransaction(act.transactionId)
                    }

                    TransactionListAction.NavigateBack -> onBackClick()

                    else -> {}
                }
            }
    }

    TransactionListScreen(
        state = state,
        onEvent = viewModel::obtainEvent,
        onCategoryTypeChanged = sharedCategoryTypeVM::categoryTypeChanged,
        onStatisticsPeriodChanged = sharedStatisticsPeriodVM::statisticsPeriodChanged
    )
}

@Composable
fun TransactionListScreen(
    state: TransactionListState,
    onEvent: (TransactionListEvent) -> Unit,
    onCategoryTypeChanged: (CategoryType) -> Unit,
    onStatisticsPeriodChanged: (StatisticsPeriod) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            HarmTopBar.HarmCommonTopBar(
                title = state.currentBalance,
                subtitle = stringResource(R.string.title_balance),
                navigationIconRes = R.drawable.ic_arrow_back_24px,
                navigationIconDesc = stringResource(R.string.ic_arrow_back_desc),
                onNavigationIconClick = { onEvent(TransactionListEvent.OnBackClick) },
                isTitleCenterAlignment = true,
            )
        },
        containerColor = HarmTheme.colors.surface,
        floatingActionButton = {
            HarmButton.HarmFloatingActionButton(
                iconRes = R.drawable.ic_add_24px,
                contentDescription = stringResource(R.string.ic_add_transaction_desc),
                onClick = {
                    onEvent(TransactionListEvent.OnFloatingButtonClick(state.categoryId))
                }
            )
        }
    ) { paddingValues ->
        ScreenWithCategoryTypeTabs(
            modifier = modifier
                .fillMaxWidth()
                .padding(paddingValues),
            tabs = CategoryType.entries.toImmutableList(),
            selectedTabIndex = state.selectedTabIndex,
            onTabClick = { categoryType ->
                onCategoryTypeChanged(categoryType)
            }
        ) {
            TransactionListContent(
                state = state,
                onEvent = onEvent,
                onStatisticsPeriodChanged = onStatisticsPeriodChanged
            )
        }
    }
}

@Composable
fun TransactionListContent(
    state: TransactionListState,
    onEvent: (TransactionListEvent) -> Unit,
    onStatisticsPeriodChanged: (StatisticsPeriod) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HarmTheme.colors.surface)
    ) {
        HarmDate.HarmStatisticPeriodList(
            data = state.statisticsDate,
            periods = StatisticsPeriod.entries.toImmutableList(),
            selectedPeriod = state.selectedStatisticsPeriod,
            onPeriodClick = { newPeriod ->
                onStatisticsPeriodChanged(newPeriod)
            }
        )

        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.title_total_amount),
                    style = HarmTheme.typography.bodyLargeSemiBold,
                    color = HarmTheme.colors.onSurface
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = state.totalAmount,
                    style = HarmTheme.typography.bodyLargeSemiBold,
                    color = HarmTheme.colors.onSurface,
                    textAlign = TextAlign.Start,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(16.dp))
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HarmMenu.HarmDropdownMenu(
                    expanded = state.isFilterMenuOpened,
                    onDismissRequest = { onEvent(TransactionListEvent.OnFilterMenuDismiss) },
                    menuOptions = state.transactionsFilters.map { category ->
                        MenuOptions(
                            text = category.name,
                            onClick = {
                                onEvent(
                                    TransactionListEvent
                                        .OnFilterMenuChanged(category)
                                )
                            }
                        )
                    }.toImmutableList()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onEvent(TransactionListEvent.OnFilterMenuClick)
                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = state.selectedFilter.name,
                            style = HarmTheme.typography.bodyLarge,
                            color = HarmTheme.colors.onSurface,
                            textAlign = TextAlign.End,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        HarmButton.HarmCardIconButton(
                            iconRes = R.drawable.ic_filter_24,
                            contentDescription = stringResource(R.string.ic_filter_desc),
                            onClick = { onEvent(TransactionListEvent.OnFilterMenuClick) }
                        )
                    }
                }
            }
        }

        if (state.oneDayTransactionsList.isEmpty()) {
            EmptyScreen()
        } else {
            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(state.oneDayTransactionsList) { day ->
                    HarmCard.HarmCardTransactionList(
                        data = day.date,
                        totalAmount = day.totalAmount,
                        transactions = day.transactions,
                        onTransactionClick = { transactionId ->
                            onEvent(TransactionListEvent.OnTransactionClick(transactionId))
                        }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(56.dp))
                }
            }
        }
    }
}

@Suppress("detekt:MagicNumber")
@Preview(showSystemUi = true)
@Composable
private fun TransactionListScreen_DarkEmptyPreview() {
    HarmTheme(darkTheme = true) {
        TransactionListScreen(
            state = TransactionListState(
                currentBalance = "20 000 ₽",
                categoryId = null,
                statisticsDate = "01.03.2026 - 31.03.2026",
                totalAmount = "0 ₽",
                oneDayTransactionsList = persistentListOf(),
                selectedFilter = TransactionsFilterUi(0, "Все категории")
            ),
            onEvent = {},
            onCategoryTypeChanged = {},
            onStatisticsPeriodChanged = {}
        )
    }
}

@Suppress("detekt:MagicNumber")
@Preview(showSystemUi = true)
@Composable
fun TransactionListScreen_LightEmptyPreview() {
    HarmTheme(darkTheme = false) {
        TransactionListScreen(
            state = TransactionListState(
                currentBalance = "20 000 ₽",
                categoryId = null,
                statisticsDate = "01.03.2026 - 31.03.2026",
                totalAmount = "0 ₽",
                oneDayTransactionsList = persistentListOf(),
                selectedFilter = TransactionsFilterUi(0, "Все категории")
            ),
            onEvent = {},
            onCategoryTypeChanged = {},
            onStatisticsPeriodChanged = {}
        )
    }
}

@Suppress("detekt:MagicNumber")
@Preview(showSystemUi = true)
@Composable
private fun TransactionListScreen_DarkPreview() {
    HarmTheme(darkTheme = true) {
        TransactionListScreen(
            state = TransactionListState(
                currentBalance = "20 000 ₽",
                categoryId = null,
                statisticsDate = "01.03.2026 - 31.03.2026",
                totalAmount = "62 000 ₽",
                oneDayTransactionsList = getExpensesTransactions(),
                selectedFilter = TransactionsFilterUi(0, "Все категории")
            ),
            onEvent = {},
            onCategoryTypeChanged = {},
            onStatisticsPeriodChanged = {}
        )
    }
}

@Suppress("detekt:MagicNumber")
@Preview(showSystemUi = true)
@Composable
private fun TransactionListScreen_LightPreview() {
    HarmTheme(darkTheme = false) {
        TransactionListScreen(
            state = TransactionListState(
                currentBalance = "20 000 ₽",
                categoryId = null,
                statisticsDate = "01.03.2026 - 31.03.2026",
                totalAmount = "62 000 ₽",
                oneDayTransactionsList = getExpensesTransactions(),
                selectedFilter = TransactionsFilterUi(0, "Все категории")
            ),
            onEvent = {},
            onCategoryTypeChanged = {},
            onStatisticsPeriodChanged = {}
        )
    }
}

@Suppress("detekt:MagicNumber")
@Preview(showSystemUi = true)
@Composable
private fun TransactionListScreen_DarkMenuPreview() {
    HarmTheme(darkTheme = true) {
        TransactionListScreen(
            state = TransactionListState(
                currentBalance = "20 000 ₽",
                categoryId = null,
                statisticsDate = "01.03.2026 - 31.03.2026",
                totalAmount = "62 000 ₽",
                oneDayTransactionsList = getExpensesTransactions(),
                selectedFilter = TransactionsFilterUi(0, "Все категории"),
                isFilterMenuOpened = true,
                transactionsFilters = getTransactionsFilter()
            ),
            onEvent = {},
            onCategoryTypeChanged = {},
            onStatisticsPeriodChanged = {}
        )
    }
}

@Suppress("detekt:MagicNumber")
@Preview(showSystemUi = true)
@Composable
private fun TransactionListScreen_LightMenuPreview() {
    HarmTheme(darkTheme = false) {
        TransactionListScreen(
            state = TransactionListState(
                currentBalance = "20 000 ₽",
                categoryId = null,
                statisticsDate = "01.03.2026 - 31.03.2026",
                totalAmount = "62 000 ₽",
                oneDayTransactionsList = getExpensesTransactions(),
                selectedFilter = TransactionsFilterUi(0, "Все категории"),
                isFilterMenuOpened = true,
                transactionsFilters = getTransactionsFilter()
            ),
            onEvent = {},
            onCategoryTypeChanged = {},
            onStatisticsPeriodChanged = {}
        )
    }
}

@Suppress("detekt:MagicNumber")
@Preview(showSystemUi = true)
@Composable
private fun TransactionListScreen_DarkFilteredPreview() {
    HarmTheme(darkTheme = true) {
        TransactionListScreen(
            state = TransactionListState(
                currentBalance = "20 000 ₽",
                categoryId = null,
                statisticsDate = "01.03.2026 - 31.03.2026",
                totalAmount = "56 500 ₽",
                oneDayTransactionsList = getFilteredTransactions(),
                selectedFilter = TransactionsFilterUi(3, "Отпуск"),
                isFilterMenuOpened = false,
            ),
            onEvent = {},
            onCategoryTypeChanged = {},
            onStatisticsPeriodChanged = {}
        )
    }
}

@Suppress("detekt:MagicNumber")
@Preview(showSystemUi = true)
@Composable
private fun TransactionListScreen_LightFilteredPreview() {
    HarmTheme(darkTheme = false) {
        TransactionListScreen(
            state = TransactionListState(
                currentBalance = "20 000 ₽",
                categoryId = null,
                statisticsDate = "01.03.2026 - 31.03.2026",
                totalAmount = "56 500 ₽",
                oneDayTransactionsList = getFilteredTransactions(),
                selectedFilter = TransactionsFilterUi(3, "Отпуск"),
                isFilterMenuOpened = false,
            ),
            onEvent = {},
            onCategoryTypeChanged = {},
            onStatisticsPeriodChanged = {}
        )
    }
}

@Suppress("detekt:MagicNumber", "detekt:LongMethod")
private fun getExpensesTransactions(): ImmutableList<OneDayTransactionsUi> {
    return persistentListOf(
        OneDayTransactionsUi(
            date = "06 Марта",
            totalAmount = "5 000 ₽",
            transactions = persistentListOf(
                TransactionUi(
                    id = 1,
                    category = CategoryUi(
                        id = 1,
                        name = "Подарки",
                        type = CategoryType.Expenses,
                        icon = CategoryIcon(
                            icon = CategoryIcons.IC_GIFT,
                            color = CategoryColors.ORANGE_T70
                        ),
                    ),
                    amount = "2 000 ₽",
                    note = "Маме на 8 марта"
                ),
                TransactionUi(
                    id = 2,
                    category = CategoryUi(
                        id = 1,
                        name = "Подарки",
                        type = CategoryType.Expenses,
                        icon = CategoryIcon(
                            icon = CategoryIcons.IC_GIFT,
                            color = CategoryColors.ORANGE_T70
                        ),
                    ),
                    amount = "1 500 ₽",
                    note = "Бабушке на 8 марта"
                ),
                TransactionUi(
                    id = 3,
                    category = CategoryUi(
                        id = 3,
                        name = "Отпуск",
                        type = CategoryType.Expenses,
                        icon = CategoryIcon(
                            icon = CategoryIcons.IC_VACATION_1,
                            color = CategoryColors.BLUE_T80
                        ),
                    ),
                    amount = "1 500 ₽",
                    note = "Чемодан"
                ),
            )
        ),
        OneDayTransactionsUi(
            date = "15 Марта",
            totalAmount = "2 000 ₽",
            transactions = persistentListOf(
                TransactionUi(
                    id = 4,
                    category = CategoryUi(
                        id = 2,
                        name = "Продукты",
                        type = CategoryType.Expenses,
                        icon = CategoryIcon(
                            icon = CategoryIcons.IC_SHOP_CART,
                            color = CategoryColors.VIOLET_T68
                        ),
                    ),
                    amount = "2 000 ₽",
                ),
            )
        ),
        OneDayTransactionsUi(
            date = "20 Марта",
            totalAmount = "30 000 ₽",
            transactions = persistentListOf(
                TransactionUi(
                    id = 5,
                    category = CategoryUi(
                        id = 3,
                        name = "Отпуск",
                        type = CategoryType.Expenses,
                        icon = CategoryIcon(
                            icon = CategoryIcons.IC_VACATION_1,
                            color = CategoryColors.BLUE_T80
                        ),
                    ),
                    amount = "30 000 ₽",
                    note = "Билеты"
                ),
            )
        ),
        OneDayTransactionsUi(
            date = "25 Марта",
            totalAmount = "25 000 ₽",
            transactions = persistentListOf(
                TransactionUi(
                    id = 6,
                    category = CategoryUi(
                        id = 3,
                        name = "Отпуск",
                        type = CategoryType.Expenses,
                        icon = CategoryIcon(
                            icon = CategoryIcons.IC_VACATION_1,
                            color = CategoryColors.BLUE_T80
                        ),
                    ),
                    amount = "25 000 ₽",
                    note = "Жилье"
                ),
            )
        ),
    )
}

@Suppress("detekt:MagicNumber")
private fun getTransactionsFilter(): ImmutableList<TransactionsFilterUi> {
    return persistentListOf(
        TransactionsFilterUi(id = 0, name = "Все категории"),
        TransactionsFilterUi(id = 1, name = "Подарки"),
        TransactionsFilterUi(id = 2, name = "Продукты"),
        TransactionsFilterUi(id = 3, name = "Отпуск"),
    ).sortedBy { it.name }.toImmutableList()
}

@Suppress("detekt:MagicNumber", "detekt:LongMethod")
private fun getFilteredTransactions(): ImmutableList<OneDayTransactionsUi> {
    return persistentListOf(
        OneDayTransactionsUi(
            date = "06 Марта",
            totalAmount = "1 500 ₽",
            transactions = persistentListOf(
                TransactionUi(
                    id = 3,
                    category = CategoryUi(
                        id = 3,
                        name = "Отпуск",
                        type = CategoryType.Expenses,
                        icon = CategoryIcon(
                            icon = CategoryIcons.IC_VACATION_1,
                            color = CategoryColors.BLUE_T80
                        ),
                    ),
                    amount = "1 500 ₽",
                    note = "Чемодан"
                ),
            )
        ),
        OneDayTransactionsUi(
            date = "20 Марта",
            totalAmount = "30 000 ₽",
            transactions = persistentListOf(
                TransactionUi(
                    id = 5,
                    category = CategoryUi(
                        id = 3,
                        name = "Отпуск",
                        type = CategoryType.Expenses,
                        icon = CategoryIcon(
                            icon = CategoryIcons.IC_VACATION_1,
                            color = CategoryColors.BLUE_T80
                        ),
                    ),
                    amount = "30 000 ₽",
                    note = "Билеты"
                ),
            )
        ),
        OneDayTransactionsUi(
            date = "25 Марта",
            totalAmount = "25 000 ₽",
            transactions = persistentListOf(
                TransactionUi(
                    id = 6,
                    category = CategoryUi(
                        id = 3,
                        name = "Отпуск",
                        type = CategoryType.Expenses,
                        icon = CategoryIcon(
                            icon = CategoryIcons.IC_VACATION_1,
                            color = CategoryColors.BLUE_T80
                        ),
                    ),
                    amount = "25 000 ₽",
                    note = "Жилье"
                ),
            )
        ),
    )
}
