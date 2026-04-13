package com.example.harmoney.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.harmoney.ui.theme.HarmTheme

@Composable
fun AppRoot() {
    val navController = rememberNavController()

    HarmTheme() {
        NavHost(
            navController = navController,
            startDestination = CategoryStatistics,
            modifier = Modifier.padding()
        ) {
            categoryStatisticsScreen(
                onNavigateToSettings = {},//drawer
                onNavigateToTransactionList = { categoryId ->
                    navController.navigateToTransactionList(categoryId)
                },
                onNavigateToCreateTransaction = {
                    navController.navigateToTransaction(
                        transactionId = null,
                        categoryId = null
                    )
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
                onNavigateToCategoryListScreen = { categoryTypeId ->
                    navController.navigateToCategoryList(categoryTypeId = categoryTypeId)
                }
            )

            categoryListScreen(
                onBackClick = { navController.popBackStack() },
                onNavigateToCreateCategory = { categoryTypeId ->
                    navController.navigateToCategory(
                        categoryId = null,
                        categoryTypeId = categoryTypeId
                    )
                },
                onNavigateToOpenCategory = { categoryId ->
                    navController.navigateToCategory(
                        categoryId = categoryId,
                        categoryTypeId = null
                    )
                }
            )

            categoryScreen(
                onBackClick = { navController.popBackStack() },
            )
        }
    }
}
