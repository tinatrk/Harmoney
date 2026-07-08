package com.example.harmoney.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun AppRoot() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = CategoryStatistics,
        modifier = Modifier.padding(),
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
    ) {
        categoryStatisticsScreen(
            onNavigateToTransactionList = { categoryId ->
                navController.navigateToTransactionList(categoryId)
            },
            onNavigateToCreateTransaction = {
                navController.navigateToTransaction(transactionId = null, categoryId = null)
            },
            onNavigateToCategoryList = {
                navController.navigateToCategoryList()
            }
        )

        transactionListScreen(
            onBackClick = { navController.popBackStack() },
            onNavigateToCreateTransaction = { categoryId ->
                navController.navigateToTransaction(
                    categoryId = categoryId,
                    transactionId = null
                )
            },
            onNavigateToOpenTransaction = { transactionId ->
                navController.navigateToTransaction(
                    categoryId = null,
                    transactionId = transactionId
                )
            }
        )

        transactionScreen(
            onBackClick = { navController.popBackStack() },
            onNavigateToCategoryScreen = { navController.navigateToCategory(categoryId = null) }
        )

        categoryListScreen(
            onNavigateToCreateCategory = {
                navController.navigateToCategory(categoryId = null)
            },
            onNavigateToOpenCategory = { categoryId ->
                navController.navigateToCategory(categoryId = categoryId)
            },
            onBackClick = { navController.popBackStack() }
        )

        categoryScreen(
            onBackClick = { navController.popBackStack() },
        )
    }
}
