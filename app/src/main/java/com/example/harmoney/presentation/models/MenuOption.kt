package com.example.harmoney.presentation.models

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable

@Immutable
data class MenuOption(
    val body:  @Composable (() -> Unit),
    val expanded: Boolean = false,
    val onClick: () -> Unit,
)
