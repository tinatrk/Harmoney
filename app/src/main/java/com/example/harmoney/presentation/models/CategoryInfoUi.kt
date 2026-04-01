package com.example.harmoney.presentation.models

import androidx.compose.runtime.Immutable
import com.example.harmoney.domain.models.CategoryIcon

@Immutable
data class CategoryInfoUi(
    val id: Long = 0,
    val name: String,
    val icon: CategoryIcon,
)
