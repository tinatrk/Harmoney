package com.example.harmoney.presentation.models

import androidx.compose.runtime.Immutable

@Immutable
data class MenuOptions(
    val text: String,
    val onClick: () -> Unit,
)
