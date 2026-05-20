package com.example.harmoney.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.harmoney.presentation.sharedViewModel.SharedCategoryTypeViewModel
import com.example.harmoney.presentation.sharedViewModel.SharedStatisticsPeriodViewModel
import com.example.harmoney.ui.screens.CategoryStatisticsScreen
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Serializable
object CategoryStatistics

fun NavGraphBuilder.categoryStatisticsScreen(
    onNavigateToTransactionList: (categoryId: Long?) -> Unit,
    onNavigateToCreateTransaction: () -> Unit,
    onNavigateToCategoryList: () -> Unit,
    sharedCategoryTypeVM: SharedCategoryTypeViewModel,
    sharedStatisticsPeriodVM: SharedStatisticsPeriodViewModel
) {
    composable<CategoryStatistics> {
        CategoryStatisticsScreen(
            sharedCategoryTypeVM = sharedCategoryTypeVM,
            sharedStatisticsPeriodVM = sharedStatisticsPeriodVM,
            onNavigateToTransactionList = onNavigateToTransactionList,
            onNavigateToCreateTransaction = onNavigateToCreateTransaction,
            onNavigateToCategoryList = onNavigateToCategoryList,
            viewModel = koinViewModel() {
                parametersOf(sharedCategoryTypeVM.selectedCategoryType.value,
                    sharedStatisticsPeriodVM.selectedStatisticsPeriod.value)
            },
        )
    }
}
