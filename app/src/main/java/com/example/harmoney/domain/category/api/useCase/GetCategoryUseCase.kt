package com.example.harmoney.domain.category.api.useCase

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.category.models.CategoryFailure
import com.example.harmoney.domain.models.Category

interface GetCategoryUseCase {
    suspend fun execute(categoryId: Long): Resource<Category, CategoryFailure>
}
