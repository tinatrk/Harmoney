package com.example.harmoney.domain.models

/**
 * - ids - объект CategoryIcons, содержащй id иконки и resIconId изображения иконки
 * - backgroundColor - цвет фона из CategoryColors
 * */
data class CategoryIcon(
    val ids: CategoryIcons,
    val backgroundColor: CategoryColors,
)
