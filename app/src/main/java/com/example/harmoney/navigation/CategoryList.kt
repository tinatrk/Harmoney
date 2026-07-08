package com.example.harmoney.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.harmoney.ui.screens.CategoryListScreen
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
data object CategoryList

fun NavGraphBuilder.categoryListScreen(
    onBackClick: () -> Unit,
    onNavigateToCreateCategory: () -> Unit,
    onNavigateToOpenCategory: (Long?) -> Unit,
) {
    composable<CategoryList> {
        CategoryListScreen(
            onNavigateToCreateCategory = onNavigateToCreateCategory,
            onNavigateToOpenCategory = onNavigateToOpenCategory,
            viewModel = koinViewModel(),
            onBackClick = onBackClick,
        )
    }
}

fun NavController.navigateToCategoryList() {
    navigate(route = CategoryList)
}
