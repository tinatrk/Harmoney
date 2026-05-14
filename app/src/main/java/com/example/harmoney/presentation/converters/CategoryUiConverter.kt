package com.example.harmoney.presentation.converters

import com.example.harmoney.domain.models.Category
import com.example.harmoney.presentation.models.CategoryUi
import kotlinx.collections.immutable.ImmutableList

interface CategoryUiConverter {
    fun map(category: Category): CategoryUi
    fun map(category: CategoryUi): Category
    fun map(categories: List<Category>): ImmutableList<CategoryUi>
}
