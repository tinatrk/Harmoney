package com.example.harmoney.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.harmoney.presentation.sharedViewModel.SharedCategoryTypeViewModel
import com.example.harmoney.presentation.sharedViewModel.SharedStatisticsPeriodViewModel
import com.example.harmoney.ui.theme.HarmTheme
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppRoot() {
    val navController = rememberNavController()
    // эта ViewModel используется на всех экранах
    val sharedCategoryTypeVM: SharedCategoryTypeViewModel = koinViewModel()

    HarmTheme() {
        NavHost(
            navController = navController,
            startDestination = MainFlow,
            modifier = Modifier.padding(),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
        ) {
            composable<MainFlow> {
                StatisticsPeriodFlow(
                    sharedCategoryTypeVM = sharedCategoryTypeVM,
                    parentNavController = navController
                )
            }

            transactionScreen(
                sharedCategoryTypeVM = sharedCategoryTypeVM,
                onBackClick = { navController.popBackStack() },
                onNavigateToCategoryListScreen = {
                    navController.navigateToCategoryList(isCategorySelectionMode = true)
                }
            )

            categoryListScreen(
                sharedCategoryTypeVM = sharedCategoryTypeVM,
                onNavigateToCreateCategory = {
                    navController.navigateToCategory(
                        categoryId = null
                    )
                },
                onNavigateToOpenCategory = { categoryId ->
                    navController.navigateToCategory(
                        categoryId = categoryId
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
                sharedCategoryTypeVM = sharedCategoryTypeVM,
                onBackClick = { navController.popBackStack() },
            )
        }
    }
}

// Два экрана вынесены, т.к. для них используется своя sharedViewModel
@Serializable
object MainFlow

@Composable
fun StatisticsPeriodFlow(
    sharedCategoryTypeVM: SharedCategoryTypeViewModel,
    parentNavController: NavController
) {
    val flowNavController = rememberNavController()

    val sharedStatisticsPeriodVM: SharedStatisticsPeriodViewModel = koinViewModel()

    NavHost(
        navController = flowNavController,
        startDestination = CategoryStatistics,
        modifier = Modifier.padding(),
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
    ) {
        categoryStatisticsScreen(
            sharedCategoryTypeVM = sharedCategoryTypeVM,
            sharedStatisticsPeriodVM = sharedStatisticsPeriodVM,
            onNavigateToTransactionList = { categoryId ->
                flowNavController.navigateToTransactionList(categoryId)
            },
            onNavigateToCreateTransaction = {
                parentNavController.navigateToTransaction(
                    transactionId = null,
                    categoryId = null
                )
            },
            onNavigateToCategoryList = {
                parentNavController.navigateToCategoryList(isCategorySelectionMode = false)
            }
        )

        transactionListScreen(
            sharedCategoryTypeVM = sharedCategoryTypeVM,
            sharedStatisticsPeriodVM = sharedStatisticsPeriodVM,
            onBackClick = { flowNavController.popBackStack() },
            onNavigateToCreateTransaction = { categoryId ->
                parentNavController.navigateToTransaction(
                    categoryId = categoryId,
                    transactionId = null
                )
            },
            onNavigateToOpenTransaction = { transactionId ->
                parentNavController.navigateToTransaction(
                    categoryId = null,
                    transactionId = transactionId
                )
            }
        )
    }
}
