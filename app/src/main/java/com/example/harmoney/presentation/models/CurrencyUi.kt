package com.example.harmoney.presentation.models

import androidx.compose.runtime.Immutable
import com.example.harmoney.domain.models.Currency

@Immutable
data class CurrencyUi(
    val currency: Currency,
    val name: String
)
