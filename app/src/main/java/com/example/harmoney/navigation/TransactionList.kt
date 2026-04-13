package com.example.harmoney.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.harmoney.ui.screens.TransactionListScreen
import kotlinx.serialization.Serializable

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
            categoryId = route.categoryId,
            onBackClick = onBackClick,
            onNavigateToCreateTransaction = onNavigateToCreateTransaction,
            onNavigateToOpenTransaction = onNavigateToOpenTransaction
        )
    }
}

fun NavController.navigateToTransactionList(categoryId: Long?) {
    navigate(route = TransactionList(categoryId = categoryId))
}
