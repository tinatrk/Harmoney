package com.example.harmoney.presentation.models

import androidx.compose.runtime.Immutable
import com.example.harmoney.domain.models.CategoryIcon

@Immutable
data class TransactionUi(
    val id: Long = 0,
    val categoryName: String,
    val categoryIcon: CategoryIcon,
    val amount: Double,
    val note: String = ""
)
