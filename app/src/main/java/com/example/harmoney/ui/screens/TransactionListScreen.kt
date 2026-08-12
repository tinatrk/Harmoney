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
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.StatisticsPeriodType
import com.example.harmoney.presentation.models.MenuOption
import com.example.harmoney.presentation.models.StatisticsPeriodUi
import com.example.harmoney.presentation.models.TransactionFilterUi
import com.example.harmoney.presentation.transactionList.models.TransactionListAction
import com.example.harmoney.presentation.transactionList.models.TransactionListEvent
import com.example.harmoney.presentation.transactionList.models.TransactionListState
import com.example.harmoney.presentation.transactionList.viewModel.TransactionListViewModel
import com.example.harmoney.ui.components.EmptyScreen
import com.example.harmoney.ui.components.ScreenWithCategoryTypeTabs
import com.example.harmoney.ui.other.PreviewData
import com.example.harmoney.ui.theme.HarmTheme
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Composable
fun TransactionListScreen(
    viewModel: TransactionListViewModel,
    onBackClick: () -> Unit,
    onNavigateToCreateTransaction: (categoryId: Long?) -> Unit,
    onNavigateToOpenTransaction: (transactionId: Long?) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val lifecycle = LocalLifecycleOwner.current.lifecycle

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
    )
}

@Composable
fun TransactionListScreen(
    state: TransactionListState,
    onEvent: (TransactionListEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            HarmTopBar.HarmCommonTopBar(
                title = state.currentBalance,
                subtitle = stringResource(R.string.title_balance),
                navigationIcon = {
                    HarmButton.HarmTopBarIconButton(
                        iconRes = R.drawable.ic_arrow_back_24px,
                        contentDescription = stringResource(R.string.ic_arrow_back_desc),
                        onClick = { onEvent(TransactionListEvent.OnBackClick) }
                    )
                },
                isTitleCenterAlignment = true,
            )
        },
        containerColor = HarmTheme.colors.surface,
        floatingActionButton = {
            HarmButton.HarmFloatingActionButton(
                iconRes = R.drawable.ic_add_24px,
                contentDescription = stringResource(R.string.ic_add_transaction_desc),
                onClick = { onEvent(TransactionListEvent.OnFloatingButtonClick) }
            )
        }
    ) { paddingValues ->
        ScreenWithCategoryTypeTabs(
            modifier = modifier
                .fillMaxWidth()
                .padding(paddingValues),
            tabs = CategoryType.entries.toImmutableList(),
            selectedTabIndex = state.selectedTabIndex,
            onTabClick = { categoryType -> onEvent(TransactionListEvent.OnTabClick(categoryType)) }
        ) {
            TransactionListContent(
                state = state,
                onEvent = onEvent,
            )
        }
    }
}

@Composable
fun TransactionListContent(
    state: TransactionListState,
    onEvent: (TransactionListEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HarmTheme.colors.surface)
    ) {
        HarmDate.HarmStatisticPeriodList(
            data = state.selectedStatisticsPeriod.date,
            periods = StatisticsPeriodType.entries.toImmutableList(),
            selectedPeriod = state.selectedStatisticsPeriod.type,
            onPeriodClick = { newPeriod ->
                onEvent(TransactionListEvent.OnStatisticsPeriodClick(newPeriod))
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
                    menuOptions = state.transactionsFilters.map { filter ->
                        when (filter) {
                            is TransactionFilterUi.All -> {
                                MenuOption(
                                    text = stringResource(R.string.title_all_filters),
                                    expanded = state.selectedFilter is TransactionFilterUi.All
                                )
                                { onEvent(TransactionListEvent.OnFilterMenuChanged(filter)) }
                            }

                            is TransactionFilterUi.CategoryUi -> {
                                MenuOption(
                                    text = filter.name,
                                    expanded =
                                        state.selectedFilter is TransactionFilterUi.CategoryUi
                                                && state.selectedFilter.id == filter.id
                                ) { onEvent(TransactionListEvent.OnFilterMenuChanged(filter)) }
                            }
                        }
                    }.toImmutableList(),
                    isNeededHighlightSelectedOption = true
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onEvent(TransactionListEvent.OnFilterMenuClick) },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        val selectedFilterText = when (val filter = state.selectedFilter) {
                            is TransactionFilterUi.All -> stringResource(R.string.title_all_filters)
                            is TransactionFilterUi.CategoryUi -> filter.name
                        }
                        Text(
                            text = selectedFilterText,
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
            EmptyScreen(message = stringResource(R.string.placeholder_empty_transaction_list))
        } else {
            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
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
                selectedStatisticsPeriod = StatisticsPeriodUi(
                    type = StatisticsPeriodType.CURRENT_MONTH,
                    date = "01.03.2026 - 31.03.2026"
                ),
                totalAmount = "0 ₽",
                oneDayTransactionsList = persistentListOf(),
                selectedFilter = TransactionFilterUi.All
            ),
            onEvent = {},
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
                selectedStatisticsPeriod = StatisticsPeriodUi(
                    type = StatisticsPeriodType.CURRENT_MONTH,
                    date = "01.03.2026 - 31.03.2026"
                ),
                totalAmount = "0 ₽",
                oneDayTransactionsList = persistentListOf(),
                selectedFilter = TransactionFilterUi.All
            ),
            onEvent = {},
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
                selectedStatisticsPeriod = StatisticsPeriodUi(
                    type = StatisticsPeriodType.CURRENT_MONTH,
                    date = "01.03.2026 - 31.03.2026"
                ),
                totalAmount = "62 000 ₽",
                oneDayTransactionsList = PreviewData.getExpensesTransactions(),
                selectedFilter = TransactionFilterUi.All
            ),
            onEvent = {},
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
                selectedStatisticsPeriod = StatisticsPeriodUi(
                    type = StatisticsPeriodType.CURRENT_MONTH,
                    date = "01.03.2026 - 31.03.2026"
                ),
                totalAmount = "62 000 ₽",
                oneDayTransactionsList = PreviewData.getExpensesTransactions(),
                selectedFilter = TransactionFilterUi.All
            ),
            onEvent = {},
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
                selectedStatisticsPeriod = StatisticsPeriodUi(
                    type = StatisticsPeriodType.CURRENT_MONTH,
                    date = "01.03.2026 - 31.03.2026"
                ),
                totalAmount = "62 000 ₽",
                oneDayTransactionsList = PreviewData.getExpensesTransactions(),
                selectedFilter = TransactionFilterUi.All,
                isFilterMenuOpened = true,
                transactionsFilters = PreviewData.getExpensesFilters()
            ),
            onEvent = {},
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
                selectedStatisticsPeriod = StatisticsPeriodUi(
                    type = StatisticsPeriodType.CURRENT_MONTH,
                    date = "01.03.2026 - 31.03.2026"
                ),
                totalAmount = "62 000 ₽",
                oneDayTransactionsList = PreviewData.getExpensesTransactions(),
                selectedFilter = TransactionFilterUi.All,
                isFilterMenuOpened = true,
                transactionsFilters = PreviewData.getExpensesFilters()
            ),
            onEvent = {},
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
                selectedStatisticsPeriod = StatisticsPeriodUi(
                    type = StatisticsPeriodType.CURRENT_MONTH,
                    date = "01.03.2026 - 31.03.2026"
                ),
                totalAmount = "56 500 ₽",
                oneDayTransactionsList = PreviewData.getFilteredTransactions(),
                selectedFilter = TransactionFilterUi.CategoryUi(id = 3, name = "Отпуск"),
                isFilterMenuOpened = false,
            ),
            onEvent = {},
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
                selectedStatisticsPeriod = StatisticsPeriodUi(
                    type = StatisticsPeriodType.CURRENT_MONTH,
                    date = "01.03.2026 - 31.03.2026"
                ),
                totalAmount = "56 500 ₽",
                oneDayTransactionsList = PreviewData.getFilteredTransactions(),
                selectedFilter = TransactionFilterUi.CategoryUi(id = 3, name = "Отпуск"),
                isFilterMenuOpened = false,
            ),
            onEvent = {},
        )
    }
}
