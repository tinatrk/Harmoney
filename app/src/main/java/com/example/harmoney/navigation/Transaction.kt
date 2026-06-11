package com.example.harmoney.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.harmoney.presentation.sharedViewModel.SharedCategoryTypeViewModel
import com.example.harmoney.ui.screens.TransactionScreen
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Serializable
data class Transaction(val categoryId: Long?, val transactionId: Long?)

fun NavGraphBuilder.transactionScreen(
    onBackClick: () -> Unit,
    onNavigateToCategoryListScreen: () -> Unit,
    sharedCategoryTypeVM: SharedCategoryTypeViewModel
) {
    composable<Transaction> { navBackStackEntry ->
        val route = navBackStackEntry.toRoute<Transaction>()
        TransactionScreen(
            onBackClick = onBackClick,
            onNavigateToCategoryListScreen = onNavigateToCategoryListScreen,
            viewModel = koinViewModel() {
                parametersOf(
                    sharedCategoryTypeVM.selectedCategoryType.value,
                    route.categoryId,
                    route.transactionId

                )
            },
            calculatorViewModel = koinViewModel(),
            sharedCategoryTypeViewModel = sharedCategoryTypeVM
        )
    }
}

fun NavController.navigateToTransaction(categoryId: Long?, transactionId: Long?) {
    navigate(route = Transaction(categoryId = categoryId, transactionId = transactionId))
}
