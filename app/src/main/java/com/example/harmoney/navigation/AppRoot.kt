package com.example.harmoney.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
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
            modifier = Modifier.padding(),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
        ) {
            categoryStatisticsScreen(
                onNavigateToTransactionList = { categoryId ->
                    navController.navigateToTransactionList(categoryId)
                },
                onNavigateToCreateTransaction = {
                    navController.navigateToTransaction(
                        transactionId = null,
                        categoryId = null
                    )
                },
                onNavigateToCategoryList = {
                    navController.navigateToCategoryList(categoryTypeId = null)
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
                },
                onBackWithChosenCategory = {
                    val result = navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.get<Long>(NavResultKeys.SELECTED_CATEGORY)

                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(NavResultKeys.SELECTED_CATEGORY, result)

                    navController.popBackStack()
                }
            )

            categoryScreen(
                onBackClick = { navController.popBackStack() },
            )
        }
    }
}
