package com.example.harmoney.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.example.harmoney.R
import com.example.harmoney.core.uilibrary.buttons.HarmButton
import com.example.harmoney.core.uilibrary.cards.HarmCard
import com.example.harmoney.core.uilibrary.date.HarmDate
import com.example.harmoney.core.uilibrary.topbars.HarmTopBar
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.StatisticsPeriod
import com.example.harmoney.presentation.sharedViewModel.SharedCategoryTypeViewModel
import com.example.harmoney.presentation.sharedViewModel.SharedStatisticsPeriodViewModel
import com.example.harmoney.presentation.transactionList.models.TransactionListAction
import com.example.harmoney.presentation.transactionList.models.TransactionListEvent
import com.example.harmoney.presentation.transactionList.models.TransactionListState
import com.example.harmoney.presentation.transactionList.viewModel.TransactionListViewModel
import com.example.harmoney.ui.components.EmptyScreen
import com.example.harmoney.ui.components.ScreenWithCategoryTypeTabs
import com.example.harmoney.ui.theme.HarmTheme
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
        viewModel.obtainEvent(TransactionListEvent.OnStatisticsPeriodClick(statisticsPeriod))
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
                /*onEvent(
                    TransactionListEvent
                        .OnTabClick(categoryType)
                )*/
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
                //onEvent(TransactionListEvent.OnStatisticsPeriodClick(newPeriod))
            }
        )

        if (state.oneDayTransactionsList.isEmpty()) {
            EmptyScreen()
        } else {
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth()
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
                    textAlign = TextAlign.End
                )
            }
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
