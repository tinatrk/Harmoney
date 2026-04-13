package com.example.harmoney.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.harmoney.ui.screens.CategoryStatisticsScreen
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
object CategoryStatistics

fun NavGraphBuilder.categoryStatisticsScreen(
    onNavigateToSettings: () -> Unit,
    onNavigateToTransactionList: (categoryId: Long?) -> Unit,
    onNavigateToCreateTransaction: () -> Unit,
) {
    composable<CategoryStatistics> {
        CategoryStatisticsScreen(
            onNavigateToSettings = onNavigateToSettings,
            onNavigateToTransactionList = onNavigateToTransactionList,
            onNavigateToCreateTransaction = onNavigateToCreateTransaction,
            viewModel = koinViewModel()
        )
    }
}
