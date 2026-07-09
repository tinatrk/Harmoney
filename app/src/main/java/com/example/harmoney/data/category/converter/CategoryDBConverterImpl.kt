package com.example.harmoney.data.category.converter

import com.example.harmoney.data.category.entity.CategoryEntity
import com.example.harmoney.domain.models.Category
import com.example.harmoney.domain.models.CategoryColors
import com.example.harmoney.domain.models.CategoryIcon
import com.example.harmoney.domain.models.CategoryIcons
import com.example.harmoney.domain.models.CategoryType

class CategoryDBConverterImpl : CategoryDBConverter {
    override fun map(category: Category): CategoryEntity {
        return CategoryEntity(
            id = category.id,
            name = category.name,
            typeId = category.type.id,
            iconId = category.icon.icon.id,
            iconColorId = category.icon.color.id,
            createdAt = category.createdAt,
            userOrder = category.userOrder
        )
    }

    override fun map(category: CategoryEntity): Category {
        return Category(
            id = category.id,
            name = category.name,
            type = CategoryType.fromId(category.typeId),
            icon = CategoryIcon(
                icon = CategoryIcons.fromId(category.iconId),
                color = CategoryColors.fromId(category.iconColorId)
            ),
            createdAt = category.createdAt,
            userOrder = category.userOrder
        )
    }

    override fun map(categoryList: List<CategoryEntity>): List<Category> {
        return categoryList.map { map(it) }
    }
}
