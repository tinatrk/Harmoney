package com.example.harmoney.ui.mappers

import com.example.harmoney.R
import com.example.harmoney.presentation.category.models.CategoryIconSubType

object CategoryIconSubTypeUiMapper {
    fun CategoryIconSubType.toStringRes(): Int = when (this) {
        CategoryIconSubType.COMMON -> R.string.category_icon_sub_type_common
        CategoryIconSubType.FINANCE -> R.string.category_icon_sub_type_finance
        CategoryIconSubType.HOME -> R.string.category_icon_sub_type_home
        CategoryIconSubType.HEALTH -> R.string.category_icon_sub_type_health
        CategoryIconSubType.TRANSPORT -> R.string.category_icon_sub_type_transport
        CategoryIconSubType.SPORT -> R.string.category_icon_sub_type_sport
        CategoryIconSubType.ANIMALS -> R.string.category_icon_sub_type_animals
        CategoryIconSubType.HOBBY -> R.string.category_icon_sub_type_hobby
        CategoryIconSubType.EVENTS -> R.string.category_icon_sub_type_events
        CategoryIconSubType.OTHER -> R.string.category_icon_sub_type_other
    }
}
