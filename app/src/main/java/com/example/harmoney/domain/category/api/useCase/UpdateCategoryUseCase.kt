package com.example.harmoney.domain.category.api.useCase

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.category.models.CategoryFailure
import com.example.harmoney.domain.models.Category

interface UpdateCategoryUseCase {
    suspend fun execute(category: Category): Resource<Unit, CategoryFailure>
}
