package com.example.harmoney.presentation.category.models

import androidx.compose.runtime.Immutable

@Immutable
data class MenuOptions(
    val text: String,
    val onClick: () -> Unit,
)
