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
import com.example.harmoney.presentation.transactionList.models.TransactionListAction
import com.example.harmoney.presentation.transactionList.viewModel.TransactionListViewModel
import com.example.harmoney.ui.theme.HarmTheme

@Composable
fun TransactionListScreen(
    viewModel: TransactionListViewModel,
    onBackClick: () -> Unit,
    onNavigateToCreateTransaction: (categoryId: Long?) -> Unit,
    onNavigateToOpenTransaction: (transactionId: Long?) -> Unit,
) {

    val state by viewModel.screenState.collectAsStateWithLifecycle()
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    LaunchedEffect(Unit) {
        viewModel.action
            .flowWithLifecycle(lifecycle, minActiveState = Lifecycle.State.STARTED)
            .collect { act ->
                when (act) {
                    is TransactionListAction.NavigateToCreatingTransaction -> {
                        onNavigateToCreateTransaction(act.categoryId)
                    }

                    is TransactionListAction.NavigateToOpeningTransaction -> {
                        onNavigateToOpenTransaction(act.transactionId)
                    }

                    TransactionListAction.NavigateBack -> onBackClick()

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
            text = "Transaction List Screen",
            style = HarmTheme.typography.titleLargeSemiBold,
            color = HarmTheme.colors.onSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = "categoryId = ${state.categoryId}",
            style = HarmTheme.typography.bodyLarge,
            color = HarmTheme.colors.onSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))
        HarmButton.HarmPrimaryButton(
            text = "Navigate Back",
            onClick = { viewModel.onNavigateBack() }
        )

        Spacer(modifier = Modifier.height(16.dp))
        HarmButton.HarmPrimaryButton(
            text = "Navigate To Create Transaction",
            onClick = { viewModel.onCreateTransaction(null) }
        )

        Spacer(modifier = Modifier.height(16.dp))
        HarmButton.HarmPrimaryButton(
            text = "Navigate To Create Transaction with categoryId = 2",
            onClick = { viewModel.onCreateTransaction(2) }
        )

        Spacer(modifier = Modifier.height(16.dp))
        HarmButton.HarmPrimaryButton(
            text = "Navigate To Open Transaction",
            onClick = { viewModel.onOpenTransaction(null) }
        )

        Spacer(modifier = Modifier.height(16.dp))
        HarmButton.HarmPrimaryButton(
            text = "Navigate To Open Transaction with transactionId = 3",
            onClick = { viewModel.onOpenTransaction(3) }
        )
    }
}
