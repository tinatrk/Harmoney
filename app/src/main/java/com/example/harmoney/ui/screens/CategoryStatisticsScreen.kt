package com.example.harmoney.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.example.harmoney.R
import com.example.harmoney.core.uilibrary.buttons.HarmButton
import com.example.harmoney.core.uilibrary.drawers.HarmDrawer
import com.example.harmoney.core.uilibrary.topbars.HarmTopBar
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.presentation.categoryStatistics.models.CategoryStatisticsAction
import com.example.harmoney.presentation.categoryStatistics.models.CategoryStatisticsEvent
import com.example.harmoney.presentation.categoryStatistics.models.CategoryStatisticsState
import com.example.harmoney.presentation.categoryStatistics.viewModel.CategoryStatisticsViewModel
import com.example.harmoney.ui.components.ScreenWithCategoryTypeTabs
import com.example.harmoney.ui.theme.HarmTheme
import kotlinx.coroutines.launch

@Composable
fun CategoryStatisticsScreen(
    viewModel: CategoryStatisticsViewModel,
    onNavigateToTransactionList: (categoryId: Long?) -> Unit,
    onNavigateToCreateTransaction: () -> Unit,
    onNavigateToCategoryList: () -> Unit,
) {
    val state by viewModel.screenState.collectAsStateWithLifecycle()
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

    HarmDrawer.HarmModalDrawer(
        title = stringResource(R.string.title_drawer_settings),
        drawerState = drawerState,
        drawerItems = {
            HarmDrawer.HarmDrawerItem(
                label = stringResource(R.string.title_drawer_item_theme),
                selected = false,
                onClick = {
                    viewModel.obtainEvent(
                        CategoryStatisticsEvent
                            .OnChangeTheme
                    )
                },
                badge = {
                    HarmButton.HarmSwitch(
                        isChecked = state.isThemeDark,
                        onClick = {
                            viewModel.obtainEvent(
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
                    viewModel.obtainEvent(
                        CategoryStatisticsEvent
                            .OnFirstDayMonthClick
                    )
                },
                badge = {
                    Text(
                        text = viewModel.screenState.value.firstDayMonth.toString(),
                        style = HarmTheme.typography.bodyLarge,
                        color = HarmTheme.colors.onSurface
                    )
                }
            )
            HarmDrawer.HarmDrawerItem(
                label = stringResource(R.string.title_drawer_item_category_list),
                selected = false,
                onClick = {
                    viewModel.obtainEvent(
                        CategoryStatisticsEvent
                            .OnCategoryListClick
                    )
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                HarmTopBar.HarmCommonTopBar(
                    title = stringResource(
                        R.string.pattern_money_russian,
                        state.currentBalance
                    ),
                    subtitle = stringResource(R.string.title_balance),
                    navigationIconRes = R.drawable.ic_drawer_menu_24px,
                    navigationIconDesc = stringResource(R.string.ic_drawer_menu_desc),
                    onNavigationIconClick = {
                        viewModel.obtainEvent(CategoryStatisticsEvent.OnSettingsIconClick)
                    },
                    actionIconRes = R.drawable.ic_list_24px,
                    actionIconDesc = stringResource(R.string.ic_list_desc),
                    onActionIconClick = {
                        viewModel.obtainEvent(
                            CategoryStatisticsEvent
                                .OnTransactionListIconClick
                        )
                    },
                    isTitleCenterAlignment = true
                )
            },
            containerColor = HarmTheme.colors.surface
        ) { paddingValues ->
            ScreenWithCategoryTypeTabs(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues),
                tabs = CategoryType.entries,
                selectedTabIndex = state.selectedTabIndex,
                onTabClick = { categoryType ->
                    viewModel.obtainEvent(
                        CategoryStatisticsEvent
                            .OnTabClick(categoryType)
                    )
                }
            ) {
                CategoryStatisticsContent(
                    state = state,
                    onEvent = viewModel::obtainEvent,
                )
            }
        }
    }
}

@Composable
fun CategoryStatisticsContent(
    state: CategoryStatisticsState,
    onEvent: (CategoryStatisticsEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HarmTheme.colors.surface)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = state.categoryInfo,
            style = HarmTheme.typography.bodyLarge,
            color = HarmTheme.colors.onSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        HarmButton.HarmPrimaryButton(
            text = "On category click, categoryId = 1 (transaction list)",
            onClick = { onEvent(CategoryStatisticsEvent.OnCategoryClick(1)) }
        )

        Spacer(modifier = Modifier.height(16.dp))
        HarmButton.HarmPrimaryButton(
            text = "On Floating button (create transaction)",
            onClick = { onEvent(CategoryStatisticsEvent.OnFloatingButtonClick) }
        )
    }
}
