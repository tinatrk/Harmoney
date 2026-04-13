package com.example.harmoney.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.harmoney.ui.screens.CategoryListScreen
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Serializable
data class CategoryList(val categoryTypeId: Long?)

fun NavGraphBuilder.categoryListScreen(
    onBackWithChosenCategory: () -> Unit,
    onNavigateToCreateCategory: (Long?) -> Unit,
    onNavigateToOpenCategory: (Long?) -> Unit,
) {
    composable<CategoryList> { nacBackStackEntry ->
        val route = nacBackStackEntry.toRoute<CategoryList>()
        CategoryListScreen(
            onNavigateToCreateCategory = onNavigateToCreateCategory,
            onNavigateToOpenCategory = onNavigateToOpenCategory,
            viewModel = koinViewModel() {
                parametersOf(route.categoryTypeId)
            },
            onBackClick = onBackWithChosenCategory
        )
    }
}

fun NavController.navigateToCategoryList(categoryTypeId: Long?) {
    navigate(route = CategoryList(categoryTypeId = categoryTypeId))
}
