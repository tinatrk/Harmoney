package com.example.harmoney.domain.category.api.useCase

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.category.models.CategoryFailure
import com.example.harmoney.domain.models.CategoryType

interface CheckCategoryAlreadyExistsUseCase {
    suspend fun execute(
        categoryName: String,
        categoryType: CategoryType
    ): Resource<Boolean, CategoryFailure>
}
