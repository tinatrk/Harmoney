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
import com.example.harmoney.core.uilibrary.topbars.HarmTopBar
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.presentation.category.models.CategoryAction
import com.example.harmoney.presentation.category.models.CategoryEvent
import com.example.harmoney.presentation.category.models.CategoryState
import com.example.harmoney.presentation.category.viewModel.CategoryViewModel
import com.example.harmoney.presentation.sharedViewModel.SharedCategoryTypeViewModel
import com.example.harmoney.ui.theme.HarmTheme

@Composable
fun CategoryScreen(
    sharedCategoryTypeVM: SharedCategoryTypeViewModel,
    viewModel: CategoryViewModel,
    onBackClick: () -> Unit,
) {
    val categoryType by sharedCategoryTypeVM.selectedCategoryType.collectAsStateWithLifecycle()
    val state by viewModel.screenState.collectAsStateWithLifecycle()
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    LaunchedEffect(categoryType) {
        viewModel.obtainEvent(CategoryEvent.OnChangeCategoryTypeClick(categoryType))
    }

    LaunchedEffect(Unit) {
        viewModel.action
            .flowWithLifecycle(lifecycle, minActiveState = Lifecycle.State.STARTED)
            .collect { act ->
                when (act) {
                    CategoryAction.NavigateBack -> onBackClick()
                    else -> {}
                }
            }
    }

    Scaffold(
        topBar = {
            HarmTopBar.HarmCommonTopBar(
                title = stringResource(
                    if (state.isCreateCategoryScreen) {
                        R.string.title_top_app_bar_create_category
                    } else {
                        R.string.title_top_app_bar_edit_category
                    }
                ),
                navigationIconRes = R.drawable.ic_arrow_back_24px,
                navigationIconDesc = stringResource(R.string.ic_arrow_back_desc),
                onNavigationIconClick = { viewModel.obtainEvent(CategoryEvent.OnBackClick) },
                isTitleCenterAlignment = false
            )
        },
        containerColor = HarmTheme.colors.surface
    ) { paddingValues ->
        CategoryContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            state = state,
            onEvent = viewModel::obtainEvent,
            onCategoryTypeChanged = sharedCategoryTypeVM::categoryTypeChanged
        )
    }
}

@Composable
fun CategoryContent(
    state: CategoryState,
    onEvent: (CategoryEvent) -> Unit,
    onCategoryTypeChanged: (CategoryType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HarmTheme.colors.surface)
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = "categoryId = ${state.categoryId}",
            style = HarmTheme.typography.bodyLarge,
            color = HarmTheme.colors.onSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = "categoryType = ${state.selectedCategoryType.name}",
            style = HarmTheme.typography.bodyLarge,
            color = HarmTheme.colors.onSurface,
            textAlign = TextAlign.Center
        )
    }
}
