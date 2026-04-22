package com.example.harmoney.presentation.category.models

import com.example.harmoney.domain.models.CategoryType

data class CategoryState(
    val categoryId: Long? = null,
    val isCreateCategoryScreen: Boolean = true,
    val categoryType: CategoryType = CategoryType.Expenses,
)
