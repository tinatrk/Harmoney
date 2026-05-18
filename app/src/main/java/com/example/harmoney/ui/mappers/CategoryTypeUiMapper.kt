package com.example.harmoney.ui.mappers

import com.example.harmoney.R
import com.example.harmoney.domain.models.CategoryType

object CategoryTypeUiMapper {
    fun CategoryType.toStringRes(): Int = when (this) {
        CategoryType.Expenses -> R.string.category_type_expenses_title
        CategoryType.Income -> R.string.category_type_income_title
    }
}