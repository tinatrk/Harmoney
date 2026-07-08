package com.example.harmoney.domain.settings.categorySortingMode.api.useCase

import com.example.harmoney.domain.models.SortOption
import kotlinx.coroutines.flow.Flow

interface CategorySortOptionInteractor {
    fun getSortOption(): Flow<SortOption>

    suspend fun setSortOption(sortOption: SortOption)
}
