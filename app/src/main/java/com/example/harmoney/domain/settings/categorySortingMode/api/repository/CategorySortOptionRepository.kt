package com.example.harmoney.domain.settings.categorySortingMode.api.repository

import com.example.harmoney.domain.models.SortOption
import kotlinx.coroutines.flow.Flow

interface CategorySortOptionRepository {
    fun getSortOption(): Flow<SortOption>

    suspend fun setSortOption(sortingOption: SortOption)
}

// предлагается такое использование
/*class GetCategoriesUseCaseImpl(
    private val categoryRepository: CategoryRepository,
    private val sortOptionRepository: CategorySortingRepository,
) : GetCategoriesUseCase {
    override fun invoke(): Flow<List<Category>> =
        combine(
            sortOptionRepository.getSortOption()
        ) {sortOption ->
            sortOption
        }.flatMapLatest {sortOption ->
            categoryRepository.getCategories(sortOption)
        }
}*/
