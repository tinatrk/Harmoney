package com.example.harmoney.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.harmoney.presentation.transactionList.viewModel.TransactionListViewModel
import com.example.harmoney.ui.screens.TransactionListScreen
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Serializable
data class TransactionList(val categoryId: Long?)

fun NavGraphBuilder.transactionListScreen(
    onBackClick: () -> Unit,
    onNavigateToCreateTransaction: (Long?) -> Unit,
    onNavigateToOpenTransaction: (Long?) -> Unit,
) {
    composable<TransactionList> { navBackStackEntry ->
        val route = navBackStackEntry.toRoute<TransactionList>()
        TransactionListScreen(
            onBackClick = onBackClick,
            onNavigateToCreateTransaction = onNavigateToCreateTransaction,
            onNavigateToOpenTransaction = onNavigateToOpenTransaction,
            viewModel = koinViewModel<TransactionListViewModel>() {
                parametersOf(route.categoryId)
            },
        )
    }
}

fun NavController.navigateToTransactionList(categoryId: Long?) {
    navigate(route = TransactionList(categoryId = categoryId))
}
