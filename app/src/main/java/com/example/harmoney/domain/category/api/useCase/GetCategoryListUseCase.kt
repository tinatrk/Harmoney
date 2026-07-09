package com.example.harmoney.domain.category.api.useCase

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.models.Category
import com.example.harmoney.domain.category.models.CategoryFailure
import com.example.harmoney.domain.models.CategoryType
import kotlinx.coroutines.flow.Flow

interface GetCategoryListUseCase {
    fun execute(categoryType: CategoryType): Flow<Resource<List<Category>, CategoryFailure>>
}
