package com.example.harmoney.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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
import com.example.harmoney.domain.models.SortOption
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.presentation.categoryList.models.CategoryListAction
import com.example.harmoney.presentation.categoryList.models.CategoryListEvent
import com.example.harmoney.presentation.categoryList.models.CategoryListState
import com.example.harmoney.presentation.categoryList.viewModel.CategoryListViewModel
import com.example.harmoney.presentation.models.CategoryUi
import com.example.harmoney.presentation.models.MenuOption
import com.example.harmoney.presentation.sharedViewModel.SharedCategoryTypeViewModel
import com.example.harmoney.ui.components.EmptyScreen
import com.example.harmoney.ui.components.ScreenWithCategoryTypeTabs
import com.example.harmoney.ui.mappers.CategoryIconUiMapper.toDrawableRes
import com.example.harmoney.ui.mappers.SortOptionUiMapper.toDrawableRes
import com.example.harmoney.ui.mappers.SortOptionUiMapper.toStringRes
import com.example.harmoney.ui.other.PreviewData
import com.example.harmoney.ui.theme.HarmTheme
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyGridState
import org.burnoutcrew.reorderable.reorderable

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
                        iconRes = state.selectedSortOption.toDrawableRes(),
                        contentDescription = stringResource(R.string.ic_swap_vert_desc),
                        onMenuClick = { onEvent(CategoryListEvent.OnSortMenuClick) },
                        onMenuDismiss = { onEvent(CategoryListEvent.OnSortMenuDismiss) },
                        expanded = state.isSortMenuOpened,
                        menuOptions = SortOption.entries.map { sortOption ->
                            MenuOption(
                                text = stringResource(sortOption.toStringRes()),
                                expanded = state.selectedSortOption.id == sortOption.id,
                                leadingIconRes = sortOption.toDrawableRes()
                            ) {
                                onEvent(
                                    CategoryListEvent.OnSortOptionClick(sortOption)
                                )
                            }
                        }.toImmutableList(),
                        isNeededHighlightSelectedOption = true
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
                onEvent = onEvent
            )
        }
    }
}

@Composable
fun CategoryListContent(
    state: CategoryListState,
    onEvent: (CategoryListEvent) -> Unit,
) {

    if (state.categories.isEmpty()) {
        EmptyScreen(message = stringResource(R.string.placeholder_empty_category_list))
    } else {
        if (state.selectedSortOption == SortOption.USER_ORDER) {
            VerticalReorderableGrid(state = state, onEvent = onEvent)
        } else {
            VerticalCommonGrid(state = state, onEvent = onEvent)
        }
    }
}

@Composable
fun VerticalCommonGrid(
    state: CategoryListState,
    onEvent: (CategoryListEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val gridState = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()

    LazyVerticalGrid(
        modifier = modifier
            .fillMaxSize()
            .background(HarmTheme.colors.surface)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        state = gridState
    ) {
        items(state.categories, key = { it.id }) { category ->
            CategoryItem(
                category = category,
                onCategoryClick = {
                    onEvent(CategoryListEvent.OnCategoryClick(category.id))
                }
            )
        }
    }

    LaunchedEffect(state.categories) {
        coroutineScope.launch {
            gridState.scrollToItem(0)
        }
    }
}

@Composable
fun VerticalReorderableGrid(
    state: CategoryListState,
    onEvent: (CategoryListEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    var localCategories by rememberSaveable { mutableStateOf(state.categories.toList()) }
    var isDraggingAnything by remember { mutableStateOf(false) }

    LaunchedEffect(state.categories) {
        if (!isDraggingAnything) {
            localCategories = state.categories.toList()
        }
    }

    val reorderableState = rememberReorderableLazyGridState(
        onMove = { from, to ->
            isDraggingAnything = true
            localCategories = localCategories.toMutableList().apply {
                add(to.index, removeAt(from.index))
            }
        },
        onDragEnd = { from, to ->
            isDraggingAnything = false
            onEvent(CategoryListEvent.OnCategoryUserOrderChanged(from, to))
        }
    )

    LazyVerticalGrid(
        state = reorderableState.gridState,
        modifier = modifier
            .fillMaxSize()
            .background(HarmTheme.colors.surface)
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .reorderable(reorderableState)
            .detectReorderAfterLongPress(reorderableState),
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(localCategories, key = { it.id }) { category ->
            ReorderableItem(
                reorderableState,
                key = category.id,
                defaultDraggingModifier = Modifier,
                orientationLocked = false,
            ) { isDragging ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = if (isDragging) {
                                HarmTheme.colors.surfaceContainerLow
                            } else {
                                HarmTheme.colors.surfaceContainer
                            },
                            shape = RoundedCornerShape(16.dp)
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            painter = painterResource(R.drawable.ic_double_drag_handle_24px),
                            contentDescription =
                                stringResource(R.string.ic_double_drag_handle_desc),
                            tint = HarmTheme.colors.onSurface
                        )
                    }
                    CategoryItem(
                        category = category,
                        onCategoryClick = {
                            onEvent(CategoryListEvent.OnCategoryClick(category.id))
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryItem(
    category: CategoryUi,
    onCategoryClick: () -> Unit,
) {
    HarmButton.HarmCircularIconButtonWithTitle(
        modifier = Modifier,
        iconRes = category.icon.icon.toDrawableRes(),
        iconBackground = Color(category.icon.color.background),
        iconTitle = category.name,
        contentDescription =
            stringResource(R.string.ic_category_edit_desc, category.name),
        onClick = { onCategoryClick() }
    )
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
