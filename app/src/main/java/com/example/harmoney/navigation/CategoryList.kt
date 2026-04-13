package com.example.harmoney.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.harmoney.ui.screens.CategoryListScreen
import kotlinx.serialization.Serializable

@Serializable
data class CategoryList(val categoryTypeId: Long?)

fun NavGraphBuilder.categoryListScreen(
    onBackClick: () -> Unit,
    onNavigateToCreateCategory: (Long?) -> Unit,
    onNavigateToOpenCategory: (Long?) -> Unit,
) {
    composable<CategoryList> { nacBackStackEntry ->
        val route = nacBackStackEntry.toRoute<CategoryList>()
        CategoryListScreen(
            categoryTypeId = route.categoryTypeId,
            onBackClick = onBackClick,
            onNavigateToCreateCategory = onNavigateToCreateCategory,
            onNavigateToOpenCategory = onNavigateToOpenCategory
        )
    }
}

fun NavController.navigateToCategoryList(categoryTypeId: Long?) {
    navigate(route = CategoryList(categoryTypeId = categoryTypeId))
}
