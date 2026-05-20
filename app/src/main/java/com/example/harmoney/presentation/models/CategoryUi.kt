package com.example.harmoney.presentation.models

import androidx.compose.runtime.Immutable
import com.example.harmoney.domain.models.CategoryIcon
import com.example.harmoney.domain.models.CategoryType

@Immutable
data class CategoryUi(
    val id: Long = 0,
    val name: String,
    val type: CategoryType,
    val icon: CategoryIcon,
    val createdAt: Long = 0L,
    val userOrder: Double = 0.0
)
