package com.example.harmoney.domain.category.api.useCase

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.category.models.CategoryFailure
import com.example.harmoney.domain.models.Category
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.SortOption
import kotlinx.coroutines.flow.Flow

interface GetCategoryListUseCase {
    fun execute(
        categoryType: CategoryType,
        sortOption: SortOption
    ): Flow<Resource<List<Category>, CategoryFailure>>
}
