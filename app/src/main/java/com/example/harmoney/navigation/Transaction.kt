package com.example.harmoney.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.harmoney.ui.screens.TransactionScreen
import kotlinx.serialization.Serializable

@Serializable
data class Transaction(val categoryId: Long?, val transactionId: Long?)

fun NavGraphBuilder.transactionScreen(
    onBackClick: () -> Unit,
    onNavigateToCategoryListScreen: (Long?) -> Unit,
) {
    composable<Transaction> { navBackStackEntry ->
        val route = navBackStackEntry.toRoute<Transaction>()
        TransactionScreen(
            categoryId = route.categoryId,
            transactionId = route.transactionId,
            onBackClick = onBackClick,
            onNavigateToCategoryListScreen = onNavigateToCategoryListScreen
        )
    }
}

fun NavController.navigateToTransaction(categoryId: Long?, transactionId: Long?) {
    navigate(route = Transaction(categoryId = categoryId, transactionId = transactionId))
}
