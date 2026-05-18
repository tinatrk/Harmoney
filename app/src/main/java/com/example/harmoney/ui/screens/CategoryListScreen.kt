package com.example.harmoney.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.example.harmoney.core.uilibrary.topbars.HarmTopBar
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.presentation.categoryList.models.CategoryListAction
import com.example.harmoney.presentation.categoryList.models.CategoryListEvent
import com.example.harmoney.presentation.categoryList.models.CategoryListState
import com.example.harmoney.presentation.categoryList.viewModel.CategoryListViewModel
import com.example.harmoney.ui.components.ScreenWithCategoryTypeTabs
import com.example.harmoney.ui.theme.HarmTheme
import kotlinx.collections.immutable.toImmutableList

@Composable
fun CategoryListScreen(
    viewModel: CategoryListViewModel,
    onBackClick: () -> Unit,
    onNavigateToCreateCategory: (categoryTypeId: Long?) -> Unit,
    onNavigateToOpenCategory: (categoryId: Long?) -> Unit,
) {
    val state by viewModel.screenState.collectAsStateWithLifecycle()
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    LaunchedEffect(Unit) {
        viewModel.action
            .flowWithLifecycle(lifecycle, minActiveState = Lifecycle.State.STARTED)
            .collect { act ->
                when (act) {
                    is CategoryListAction.NavigateToCreatingCategory -> {
                        onNavigateToCreateCategory(act.categoryTypeId)
                    }

                    is CategoryListAction.NavigateToOpeningCategory -> {
                        onNavigateToOpenCategory(act.categoryId)
                    }

                    CategoryListAction.NavigateBack -> onBackClick()
                    else -> {}
                }
            }
    }

    Scaffold(
        topBar = {
            HarmTopBar.HarmCommonTopBar(
                title = stringResource(R.string.title_top_app_bar_category_list),
                navigationIconRes = R.drawable.ic_arrow_back_24px,
                navigationIconDesc = stringResource(R.string.ic_arrow_back_desc),
                onNavigationIconClick = {
                    viewModel.obtainEvent(CategoryListEvent.OnBackClick)
                },
                actionIconRes = R.drawable.ic_swap_vert_24px,
                actionIconDesc = stringResource(R.string.ic_swap_vert_desc),
                onActionIconClick = {},
                isTitleCenterAlignment = false
            )
        },
        containerColor = HarmTheme.colors.surface
    ) { paddingValues ->
        ScreenWithCategoryTypeTabs(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues),
            tabs = CategoryType.entries.toImmutableList(),
            selectedTabIndex = state.selectedTabIndex,
            onTabClick = { categoryType ->
                viewModel.obtainEvent(
                    CategoryListEvent
                        .OnTabClick(categoryType)
                )
            }
        ) {
            CategoryListContent(
                state = state,
                onEvent = viewModel::obtainEvent,
            )
        }
    }
}

@Composable
fun CategoryListContent(
    state: CategoryListState,
    onEvent: (CategoryListEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val testCategoryId = 7L

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HarmTheme.colors.surface)
            .padding(horizontal = 16.dp, vertical = 16.dp)
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
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = "categoryTypeId = ${state.categoryType.id}",
            style = HarmTheme.typography.bodyLarge,
            color = HarmTheme.colors.onSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))
        HarmButton.HarmPrimaryButton(
            text = "Choose category, categoryId = 7",
            onClick = { onEvent(CategoryListEvent.OnCategoryClick(testCategoryId)) }
        )

        Spacer(modifier = Modifier.height(16.dp))
        HarmButton.HarmPrimaryButton(
            text = "On floating button click (create category)",
            onClick = { onEvent(CategoryListEvent.OnFloatingButtonClick) }
        )
    }
}
