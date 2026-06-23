package com.example.harmoney.presentation.models

import androidx.compose.runtime.Immutable

@Immutable
data class MenuOption(
    val text: String,
    val leadingIconRes: Int? = null,
    val expanded: Boolean = false,
    val onClick: () -> Unit,
)
