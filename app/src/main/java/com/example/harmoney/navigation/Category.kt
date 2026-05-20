package com.example.harmoney.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.harmoney.presentation.sharedViewModel.SharedCategoryTypeViewModel
import com.example.harmoney.ui.screens.CategoryScreen
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Serializable
data class Category(val categoryId: Long?)

fun NavGraphBuilder.categoryScreen(
    onBackClick: () -> Unit,
    sharedCategoryTypeVM: SharedCategoryTypeViewModel
) {
    composable<Category> { navBackStackEntry ->
        val route = navBackStackEntry.toRoute<Category>()
        CategoryScreen(
            onBackClick = onBackClick,
            viewModel = koinViewModel() {
                parametersOf(
                    sharedCategoryTypeVM.selectedCategoryType.value,
                    route.categoryId
                )
            },
            sharedCategoryTypeVM = sharedCategoryTypeVM
        )
    }
}

fun NavController.navigateToCategory(categoryId: Long?) {
    navigate(route = Category(categoryId = categoryId))
}
