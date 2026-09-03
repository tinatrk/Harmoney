package com.example.harmoney.presentation.categoryList.models

import androidx.compose.runtime.Stable
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.SortOption
import com.example.harmoney.presentation.models.CategoryUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Stable
data class CategoryListState(
    val selectedCategoryType: CategoryType = CategoryType.EXPENSES,
    val selectedTabIndex: Int = CategoryType.EXPENSES.ordinal,

    val selectedSortOption: SortOption = SortOption.ALPHABET,
    val isSortMenuOpened: Boolean = false,

    val categories: ImmutableList<CategoryUi> = persistentListOf(),

    val isDataLoadingError: Boolean = false
)
