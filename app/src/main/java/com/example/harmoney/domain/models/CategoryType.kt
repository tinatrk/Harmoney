package com.example.harmoney.domain.models

import com.example.harmoney.base.IdEnum
import com.example.harmoney.base.IdEnumRegistry

enum class CategoryType(override val id: Long) : IdEnum {
    Expenses(id = 1),
    Income(id = 2);

    companion object {
        private val registry = IdEnumRegistry(CategoryType::class.java) { it.id }
        fun fromId(id: Long) = registry.fromId(id)
    }
}
