package com.example.harmoney.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.harmoney.presentation.calculator.viewModel.CalculatorViewModel
import com.example.harmoney.presentation.transaction.viewModel.TransactionViewModel
import com.example.harmoney.ui.screens.TransactionScreen
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Serializable
data class Transaction(val categoryId: Long?, val transactionId: Long?)

fun NavGraphBuilder.transactionScreen(
    onBackClick: () -> Unit,
    onNavigateToCategoryScreen: () -> Unit,
) {
    composable<Transaction> { navBackStackEntry ->
        val route = navBackStackEntry.toRoute<Transaction>()
        TransactionScreen(
            onBackClick = onBackClick,
            onNavigateToCategoryScreen = onNavigateToCategoryScreen,
            viewModel = koinViewModel<TransactionViewModel>() {
                parametersOf(route.categoryId, route.transactionId)
            },
            calculatorViewModel = koinViewModel<CalculatorViewModel>(),
        )
    }
}

fun NavController.navigateToTransaction(categoryId: Long?, transactionId: Long?) {
    navigate(route = Transaction(categoryId = categoryId, transactionId = transactionId))
}
