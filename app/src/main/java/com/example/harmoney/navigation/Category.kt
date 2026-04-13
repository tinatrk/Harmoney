package com.example.harmoney.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.harmoney.ui.screens.CategoryScreen
import kotlinx.serialization.Serializable

@Serializable
data class Category(val categoryId: Long?, val categoryTypeId: Long?)

fun NavGraphBuilder.categoryScreen(
    onBackClick: () -> Unit,
) {
    composable<Category> { navBackStackEntry ->
        val route = navBackStackEntry.toRoute<Category>()
        CategoryScreen(
            categoryId = route.categoryId,
            categoryTypeId = route.categoryTypeId,
            onBackClick = onBackClick
        )
    }
}

fun NavController.navigateToCategory(categoryId: Long?, categoryTypeId: Long?) {
    navigate(route = Category(categoryId = categoryId, categoryTypeId = categoryTypeId))
}
