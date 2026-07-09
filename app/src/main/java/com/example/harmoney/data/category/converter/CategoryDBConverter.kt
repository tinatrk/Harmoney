package com.example.harmoney.data.category.converter

import com.example.harmoney.data.category.entity.CategoryEntity
import com.example.harmoney.domain.models.Category

interface CategoryDBConverter {
    fun map(category: CategoryEntity): Category

    fun map(category: Category): CategoryEntity

    fun map(categoryList: List<CategoryEntity>): List<Category>
}
