package com.example.harmoney.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.harmoney.presentation.sharedViewModel.SharedCategoryTypeViewModel
import com.example.harmoney.ui.screens.CategoryListScreen
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Serializable
data class CategoryList(val isCategorySelectionMode: Boolean)

fun NavGraphBuilder.categoryListScreen(
    onBackWithChosenCategory: () -> Unit,
    onNavigateToCreateCategory: () -> Unit,
    onNavigateToOpenCategory: (Long?) -> Unit,
    sharedCategoryTypeVM: SharedCategoryTypeViewModel
) {
    composable<CategoryList> { nacBackStackEntry ->
        val route = nacBackStackEntry.toRoute<CategoryList>()
        CategoryListScreen(
            onNavigateToCreateCategory = onNavigateToCreateCategory,
            onNavigateToOpenCategory = onNavigateToOpenCategory,
            viewModel = koinViewModel() {
                parametersOf(
                    sharedCategoryTypeVM.selectedCategoryType.value,
                    route.isCategorySelectionMode,
                )
            },
            onBackClick = onBackWithChosenCategory,
            sharedCategoryTypeVM = sharedCategoryTypeVM
        )
    }
}

fun NavController.navigateToCategoryList(isCategorySelectionMode: Boolean) {
    navigate(route = CategoryList(isCategorySelectionMode = isCategorySelectionMode))
}
