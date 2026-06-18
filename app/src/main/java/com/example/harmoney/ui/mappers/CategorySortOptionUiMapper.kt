package com.example.harmoney.ui.mappers

import com.example.harmoney.R
import com.example.harmoney.domain.models.CategorySortOption

object CategorySortOptionUiMapper {
    fun CategorySortOption.toStringRes(): Int = when (this) {
        CategorySortOption.TIME_CREATED -> R.string.title_sort_time_created
        CategorySortOption.ALPHABET -> R.string.title_sort_alphabet
        CategorySortOption.USER_ORDER -> R.string.title_sort_user_order
    }
}
