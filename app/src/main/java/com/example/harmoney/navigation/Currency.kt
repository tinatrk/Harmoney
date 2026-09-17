package com.example.harmoney.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.harmoney.ui.screens.CurrencyScreen
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Serializable
data class Currency(val currencyId: Long)

fun NavGraphBuilder.currencyScreen(
    onBackWithChosenCurrency: () -> Unit,
) {
    composable<Currency> { navBackStackEntry ->
        val route = navBackStackEntry.toRoute<Currency>()
        CurrencyScreen(
            viewModel = koinViewModel() { parametersOf(route.currencyId) },
            onBackClick = onBackWithChosenCurrency
        )
    }
}

fun NavController.navigateToCurrency(currencyId: Long) {
    navigate(route = Currency(currencyId))
}
