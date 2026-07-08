package com.example.harmoney.data.settings.categorySortingMode.impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import com.example.harmoney.domain.models.SortOption
import com.example.harmoney.domain.settings.categorySortingMode.api.repository.CategorySortOptionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CategorySortOptionRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : CategorySortOptionRepository {
    override fun getSortOption(): Flow<SortOption> =
        dataStore.data.map { prefs ->
            SortOption.fromId(prefs[CATEGORY_SORT_OPTION_ID_KEY] ?: SortOption.ALPHABET.id)
        }

    override suspend fun setSortOption(sortingOption: SortOption) {
        dataStore.edit { prefs ->
            prefs[CATEGORY_SORT_OPTION_ID_KEY] = sortingOption.id
        }
    }

    companion object {
        private val CATEGORY_SORT_OPTION_ID_KEY =
            longPreferencesKey("category_sort_option_id")
    }
}
