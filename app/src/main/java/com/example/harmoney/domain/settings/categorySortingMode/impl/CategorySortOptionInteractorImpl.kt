package com.example.harmoney.domain.settings.categorySortingMode.impl

import com.example.harmoney.domain.models.SortOption
import com.example.harmoney.domain.settings.categorySortingMode.api.repository.CategorySortOptionRepository
import com.example.harmoney.domain.settings.categorySortingMode.api.useCase.CategorySortOptionInteractor
import kotlinx.coroutines.flow.Flow

class CategorySortOptionInteractorImpl(private val repository: CategorySortOptionRepository) :
    CategorySortOptionInteractor {
    override fun getSortOption(): Flow<SortOption> {
        return repository.getSortOption()
    }

    override suspend fun setSortOption(sortOption: SortOption) {
        repository.setSortOption(sortOption)
    }
}
