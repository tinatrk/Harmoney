package com.example.harmoney.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.example.harmoney.core.uilibrary.buttons.HarmButton
import com.example.harmoney.presentation.categoryStatistics.models.CategoryStatisticsAction
import com.example.harmoney.presentation.categoryStatistics.viewModel.CategoryStatisticsViewModel
import com.example.harmoney.ui.theme.HarmTheme

@Composable
fun CategoryStatisticsScreen(
    viewModel: CategoryStatisticsViewModel,
    onNavigateToSettings: () -> Unit,
    onNavigateToTransactionList: (categoryId: Long?) -> Unit,
    onNavigateToCreateTransaction: () -> Unit,
) {
    val state by viewModel.screenState.collectAsStateWithLifecycle()
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    LaunchedEffect(Unit) {
        viewModel.action
            .flowWithLifecycle(lifecycle, minActiveState = Lifecycle.State.STARTED)
            .collect { act ->
                when (act) {
                    is CategoryStatisticsAction.NavigateToTransactionList -> {
                        onNavigateToTransactionList(act.categoryId)
                    }

                    CategoryStatisticsAction.NavigateToTransaction -> onNavigateToCreateTransaction()
                    CategoryStatisticsAction.NavigateToSettings -> onNavigateToSettings()

                    else -> {}
                }
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HarmTheme.colors.surface)
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = "Category Statistics Screen",
            style = HarmTheme.typography.titleLargeSemiBold,
            color = HarmTheme.colors.onSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        HarmButton.HarmPrimaryButton(
            text = "Navigate to TransactionList",
            onClick = { viewModel.onNavigateToTransactionList(null) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        HarmButton.HarmPrimaryButton(
            text = "Navigate to TransactionList with categoryId = 1",
            onClick = { viewModel.onNavigateToTransactionList(1) }
        )

        Spacer(modifier = Modifier.height(16.dp))
        HarmButton.HarmPrimaryButton(
            text = "Navigate to Transaction",
            onClick = { viewModel.onNavigateToTransaction() }
        )
    }
}
