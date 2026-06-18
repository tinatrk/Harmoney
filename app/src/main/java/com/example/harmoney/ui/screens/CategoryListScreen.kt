package com.example.harmoney.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.example.harmoney.R
import com.example.harmoney.core.uilibrary.buttons.HarmButton
import com.example.harmoney.core.uilibrary.topbars.HarmTopBar
import com.example.harmoney.domain.models.CategorySortOption
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.presentation.categoryList.models.CategoryListAction
import com.example.harmoney.presentation.categoryList.models.CategoryListEvent
import com.example.harmoney.presentation.categoryList.models.CategoryListState
import com.example.harmoney.presentation.categoryList.viewModel.CategoryListViewModel
import com.example.harmoney.presentation.models.MenuOptions
import com.example.harmoney.presentation.sharedViewModel.SharedCategoryTypeViewModel
import com.example.harmoney.ui.components.ScreenWithCategoryTypeTabs
import com.example.harmoney.ui.mappers.CategoryIconUiMapper.toDrawableRes
import com.example.harmoney.ui.mappers.CategorySortOptionUiMapper.toStringRes
import com.example.harmoney.ui.other.PreviewData
import com.example.harmoney.ui.theme.HarmTheme
import kotlinx.collections.immutable.toImmutableList

@Composable
fun CategoryListScreen(
    sharedCategoryTypeVM: SharedCategoryTypeViewModel,
    viewModel: CategoryListViewModel,
    onBackClick: () -> Unit,
    onNavigateToCreateCategory: () -> Unit,
    onNavigateToOpenCategory: (categoryId: Long?) -> Unit,
) {
    val categoryType by sharedCategoryTypeVM.selectedCategoryType.collectAsStateWithLifecycle()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    LaunchedEffect(categoryType) {
        viewModel.obtainEvent(CategoryListEvent.OnTabClick(categoryType))
    }

    LaunchedEffect(Unit) {
        viewModel.action
            .flowWithLifecycle(lifecycle, minActiveState = Lifecycle.State.STARTED)
            .collect { act ->
                when (act) {
                    is CategoryListAction.NavigateToCreatingCategory -> {
                        onNavigateToCreateCategory()
                    }

                    is CategoryListAction.NavigateToOpeningCategory -> {
                        onNavigateToOpenCategory(act.categoryId)
                    }

                    CategoryListAction.NavigateBack -> onBackClick()
                    else -> {}
                }
            }
    }

    CategoryListScreen(
        state = state,
        onEvent = viewModel::obtainEvent,
        onCategoryTypeChanged = sharedCategoryTypeVM::categoryTypeChanged
    )
}

@Composable
fun CategoryListScreen(
    state: CategoryListState,
    onEvent: (CategoryListEvent) -> Unit,
    onCategoryTypeChanged: (CategoryType) -> Unit,
) {
    Scaffold(
        topBar = {
            HarmTopBar.HarmCommonTopBar(
                title = stringResource(R.string.title_top_app_bar_category_list),
                navigationIcon = {
                    HarmButton.HarmTopBarIconButton(
                        iconRes = R.drawable.ic_arrow_back_24px,
                        contentDescription = stringResource(R.string.ic_arrow_back_desc),
                        onClick = { onEvent(CategoryListEvent.OnBackClick) }
                    )
                },
                actionIcons = {
                    HarmButton.HarmDropdownMenuIcon(
                        iconRes = R.drawable.ic_swap_vert_24px,
                        contentDescription = stringResource(R.string.ic_swap_vert_desc),
                        onMenuClick = { onEvent(CategoryListEvent.OnSortMenuClick) },
                        onMenuDismiss = { onEvent(CategoryListEvent.OnSortMenuDismiss) },
                        expanded = state.isSortMenuOpened,
                        menuOptions = CategorySortOption.entries.map { sortOption ->
                            MenuOptions(
                                text = stringResource(sortOption.toStringRes()),
                                onClick = {
                                    onEvent(
                                        CategoryListEvent
                                            .OnSortOptionClick(sortOption)
                                    )
                                }
                            )
                        }.toImmutableList()
                    )
                },
                isTitleCenterAlignment = false
            )
        },
        floatingActionButton = {
            HarmButton.HarmFloatingActionButton(
                iconRes = R.drawable.ic_add_24px,
                contentDescription = stringResource(R.string.ic_add_category_desc),
                onClick = { onEvent(CategoryListEvent.OnFloatingButtonClick) }
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
            onTabClick = onCategoryTypeChanged
        ) {
            CategoryListContent(
                state = state,
                onEvent = onEvent,
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
    LazyVerticalGrid(
        modifier = modifier
            .fillMaxSize()
            .background(HarmTheme.colors.surface)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(state.categories) { category ->
            HarmButton.HarmCircularIconButtonWithTitle(
                iconRes = category.icon.icon.toDrawableRes(),
                iconBackground = Color(category.icon.color.background),
                iconTitle = category.name,
                contentDescription =
                    stringResource(R.string.ic_category_edit_desc, category.name),
                onClick = { onEvent(CategoryListEvent.OnCategoryClick(category.id)) }
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun CategoryListScreen_DarkPreview() {
    HarmTheme(darkTheme = true) {
        CategoryListScreen(
            state = CategoryListState(
                categories = PreviewData.getExpensesCategories()
            ),
            onEvent = {},
            onCategoryTypeChanged = {}
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun CategoryListScreen_LightPreview() {
    HarmTheme(darkTheme = false) {
        CategoryListScreen(
            state = CategoryListState(
                categories = PreviewData.getExpensesCategories()
            ),
            onEvent = {},
            onCategoryTypeChanged = {}
        )
    }
}
