package com.example.harmoney.ui.mappers

import com.example.harmoney.R
import com.example.harmoney.domain.models.SortOption

object SortOptionUiMapper {
    fun SortOption.toStringRes(): Int = when (this) {
        SortOption.TIME_CREATED -> R.string.title_sort_time_created
        SortOption.ALPHABET -> R.string.title_sort_alphabet
        SortOption.USER_ORDER -> R.string.title_sort_user_order
    }

    fun SortOption.toDrawableRes(): Int = when (this) {
        SortOption.TIME_CREATED -> R.drawable.ic_sort_numeric_24
        SortOption.ALPHABET -> R.drawable.ic_sort_by_alpha_24px
        SortOption.USER_ORDER -> R.drawable.ic_user_sort_24px
    }
}
