package com.example.harmoney.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.harmoney.presentation.sharedViewModel.SharedCategoryTypeViewModel
import com.example.harmoney.ui.screens.CategoryListScreen
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Serializable
data object CategoryList

fun NavGraphBuilder.categoryListScreen(
    onBackClick: () -> Unit,
    onNavigateToCreateCategory: () -> Unit,
    onNavigateToOpenCategory: (Long?) -> Unit,
    sharedCategoryTypeVM: SharedCategoryTypeViewModel
) {
    composable<CategoryList> {
        CategoryListScreen(
            onNavigateToCreateCategory = onNavigateToCreateCategory,
            onNavigateToOpenCategory = onNavigateToOpenCategory,
            viewModel = koinViewModel() {
                parametersOf(sharedCategoryTypeVM.selectedCategoryType.value)
            },
            onBackClick = onBackClick,
            sharedCategoryTypeVM = sharedCategoryTypeVM
        )
    }
}

fun NavController.navigateToCategoryList() {
    navigate(route = CategoryList)
}
