package com.example.harmoney.domain.models

import com.example.harmoney.core.util.IdEnum
import com.example.harmoney.core.util.IdEnumRegistry

enum class CategoryType(override val id: Long) : IdEnum {
    EXPENSES(id = 1),
    INCOME(id = 2);

    companion object {
        private val registry = IdEnumRegistry(CategoryType::class.java) { it.id }
        fun fromId(id: Long) = registry.fromId(id)
    }
}
