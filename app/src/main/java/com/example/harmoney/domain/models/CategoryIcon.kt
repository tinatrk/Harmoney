package com.example.harmoney.domain.models

/**
 * - id - объект CategoryIcons, содержащй id иконки
 * - colors - цвета фона из CategoryColors
 * */
data class CategoryIcon(
    val icon: CategoryIcons,
    val color: CategoryColors,
)
