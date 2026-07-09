package com.example.harmoney.domain.settings.categorySortingMode.api.repository

import com.example.harmoney.domain.models.SortOption
import kotlinx.coroutines.flow.Flow

interface CategorySortOptionRepository {
    fun getSortOption(): Flow<SortOption>

    suspend fun setSortOption(sortingOption: SortOption)
}
