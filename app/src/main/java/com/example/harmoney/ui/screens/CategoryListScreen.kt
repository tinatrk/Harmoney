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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.harmoney.core.uilibrary.buttons.HarmButton
import com.example.harmoney.presentation.categoryList.viewModel.CategoryListViewModel
import com.example.harmoney.ui.theme.HarmTheme

@Composable
fun CategoryListScreen(
    viewModel: CategoryListViewModel,
    onBackClick: () -> Unit,
    onNavigateToCreateCategory: (categoryTypeId: Long?) -> Unit,
    onNavigateToOpenCategory: (categoryId: Long?) -> Unit,
) {
    val state by viewModel.screenState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HarmTheme.colors.surface)
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = "Category List Screen",
            style = HarmTheme.typography.titleLargeSemiBold,
            color = HarmTheme.colors.onSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = "categoryTypeId = ${state.categoryTypeId}",
            style = HarmTheme.typography.bodyLarge,
            color = HarmTheme.colors.onSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))
        HarmButton.HarmPrimaryButton(
            text = "Navigate Back",
            onClick = { onBackClick() }
        )

        Spacer(modifier = Modifier.height(16.dp))
        HarmButton.HarmPrimaryButton(
            text = "Navigate To Create Category",
            onClick = { onNavigateToCreateCategory(null) }
        )

        Spacer(modifier = Modifier.height(16.dp))
        HarmButton.HarmPrimaryButton(
            text = "Navigate To Create Category with categoryTypeId = 5",
            onClick = { onNavigateToCreateCategory(5) }
        )

        Spacer(modifier = Modifier.height(16.dp))
        HarmButton.HarmPrimaryButton(
            text = "onNavigateToOpenCategory",
            onClick = { onNavigateToOpenCategory(null) }
        )

        Spacer(modifier = Modifier.height(16.dp))
        HarmButton.HarmPrimaryButton(
            text = "onNavigateToOpenCategory with categoryId = 6",
            onClick = { onNavigateToOpenCategory(6) }
        )
    }
}
