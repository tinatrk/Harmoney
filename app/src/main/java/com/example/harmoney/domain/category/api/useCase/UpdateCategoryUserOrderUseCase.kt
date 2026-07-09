package com.example.harmoney.domain.category.api.useCase

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.category.models.CategoryFailure
import com.example.harmoney.domain.models.Category

interface UpdateCategoryUserOrderUseCase {
    suspend fun execute(
        from: Int,
        to: Int,
        oldCategories: List<Category>
    ): Resource<Unit, CategoryFailure>
}
