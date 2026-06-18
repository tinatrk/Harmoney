package com.example.harmoney.presentation.models

import androidx.compose.runtime.Immutable

@Immutable
data class MenuOptions(
    val expanded: Boolean = false,
    val text: String,
    val onClick: () -> Unit,
)
