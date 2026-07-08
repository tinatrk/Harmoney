package com.example.harmoney.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.harmoney.ui.screens.CategoryStatisticsScreen
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
object CategoryStatistics

fun NavGraphBuilder.categoryStatisticsScreen(
    onNavigateToTransactionList: (categoryId: Long?) -> Unit,
    onNavigateToCreateTransaction: () -> Unit,
    onNavigateToCategoryList: () -> Unit,
) {
    composable<CategoryStatistics> {
        CategoryStatisticsScreen(
            onNavigateToTransactionList = onNavigateToTransactionList,
            onNavigateToCreateTransaction = onNavigateToCreateTransaction,
            onNavigateToCategoryList = onNavigateToCategoryList,
            viewModel = koinViewModel(),
        )
    }
}
