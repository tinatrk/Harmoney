package com.example.harmoney.domain.models

/**
 * - ids - объект CategoryIcons, содержащй id иконки и resIconId изображения иконки
 * - colors - цвета фона и tint из CategoryColors
 * */
data class CategoryIcon(
    val ids: CategoryIcons,
    val colors: CategoryColors,
)
