package com.example.harmoney.presentation.category.models

import com.example.harmoney.domain.models.CategoryColors
import com.example.harmoney.domain.models.CategoryIcons
import com.example.harmoney.domain.models.CategoryType

data class EditableCategory(
    val name: String,
    val type: CategoryType,
    val icon: CategoryIcons,
    val iconColor: CategoryColors,
)
