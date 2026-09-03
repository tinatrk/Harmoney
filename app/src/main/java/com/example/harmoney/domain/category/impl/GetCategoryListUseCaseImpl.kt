package com.example.harmoney.domain.category.impl

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.category.api.reposiory.CategoryRepository
import com.example.harmoney.domain.category.api.useCase.GetCategoryListUseCase
import com.example.harmoney.domain.category.models.CategoryFailure
import com.example.harmoney.domain.models.Category
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.SortOption
import kotlinx.coroutines.flow.Flow

class GetCategoryListUseCaseImpl(
    private val categoryRepository: CategoryRepository
) : GetCategoryListUseCase {
    override fun execute(categoryType: CategoryType, sortOption: SortOption)
            : Flow<Resource<List<Category>, CategoryFailure>> {
        return categoryRepository.getCategoryList(categoryType, sortOption)
    }
}
