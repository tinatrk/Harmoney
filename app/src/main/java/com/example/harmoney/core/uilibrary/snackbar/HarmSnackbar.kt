package com.example.harmoney.core.uilibrary.snackbar

import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.harmoney.annotation.UiLibrary
import com.example.harmoney.ui.theme.HarmTheme

@UiLibrary
@Composable
fun HarmSnackbar(
    snackbarData: SnackbarData,
    modifier: Modifier = Modifier
) {
    Snackbar(
        modifier = modifier,
        snackbarData = snackbarData,
        containerColor = HarmTheme.colors.surfaceContainer,
        contentColor = HarmTheme.colors.onSurface,
        actionColor = HarmTheme.colors.primary
    )
}
