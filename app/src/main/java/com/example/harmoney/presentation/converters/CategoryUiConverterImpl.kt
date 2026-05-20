package com.example.harmoney.presentation.converters

import com.example.harmoney.domain.models.Category
import com.example.harmoney.presentation.models.CategoryUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

class CategoryUiConverterImpl : CategoryUiConverter {
    override fun map(category: Category): CategoryUi {
        return CategoryUi(
            id = category.id,
            name = category.name,
            icon = category.icon,
            type = category.type,
            createdAt = category.createdAt,
            userOrder = category.userOrder
        )
    }

    override fun map(category: CategoryUi): Category {
        return Category(
            id = category.id,
            name = category.name,
            icon = category.icon,
            type = category.type,
            createdAt = category.createdAt,
            userOrder = category.userOrder
        )
    }

    override fun map(categories: List<Category>): ImmutableList<CategoryUi> {
        return categories.map { map(it) }.toImmutableList()
    }
}
