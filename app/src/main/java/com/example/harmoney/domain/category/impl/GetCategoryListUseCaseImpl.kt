package com.example.harmoney.domain.category.impl

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.category.api.reposiory.CategoryRepository
import com.example.harmoney.domain.category.api.useCase.GetCategoryListUseCase
import com.example.harmoney.domain.category.models.CategoryFailure
import com.example.harmoney.domain.models.Category
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.settings.categorySortingMode.api.repository.CategorySortOptionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest

class GetCategoryListUseCaseImpl(
    private val categoryRepository: CategoryRepository,
    private val sortOptionRepository: CategorySortOptionRepository
) : GetCategoryListUseCase {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun execute(categoryType: CategoryType)
            : Flow<Resource<List<Category>, CategoryFailure>> =
        sortOptionRepository.getSortOption()
            .flatMapLatest { sortOption ->
                categoryRepository.getCategoryList(categoryType, sortOption)
            }
}
