package com.example.harmoney.domain.models

import androidx.annotation.StringRes
import com.example.harmoney.R
import com.example.harmoney.base.IdEnum
import com.example.harmoney.base.IdEnumRegistry

enum class CategoryType(override val id: Long, @param:StringRes val titleId: Int) : IdEnum {
    Expenses(id = 1, titleId = R.string.category_type_expenses_title),
    Income(id = 2, titleId = R.string.category_type_income_title);

    companion object {
        private val registry = IdEnumRegistry(CategoryType::class.java) { it.id }
        fun fromId(id: Long) = registry.fromId(id)
    }
}
