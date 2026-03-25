package com.example.harmoney.ui.theme

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

/**
 * Базовая палитра цветов приложения.
 *
 * Назначения оттенков:
 *
 * **Light**
 * - `Gray50` - surface
 * - `Purple50` - surface_container
 * - `Gray900` - on_surface/on_surface_container
 * - `Gray600` - on_surface_container_low
 * - `Purple400` - primary
 * - `Purple400` - on_primary_variant
 *
 * **Dark**
 * - `Gray_900` - surface
 * - `Gray800` - surface_container
 * - `White` - on_surface/on_surface_container
 * - `Gray400` - on_surface_container_low
 * - `Purple800` - primary
 * - `Purple800` - on_primary_variant
 *
 * **Common**
 * - `Purple100` - primary_variant
 * - `White` - on_primary
 * - `Red400` - error
 * - `Red50` - error_container
 * - `Green600` - info
 *
 */
@Stable
object HarmColor {
    val Gray50 = Color(0xFFFFFBFE)
    val Purple50 = Color(0xFFF3E5F5)
    val Gray900 = Color(0xFF1C1B1F)
    val Gray600 = Color(0xFF757575)
    val Purple400 = Color(0xFF8b22b6)

    val Gray800 = Color(0xFF49454F)
    val White = Color(0xFFFFFFFF)
    val Gray400 = Color(0xFFCAC4D0)
    val Purple800 = Color(0xFF5d1a99)

    val Purple100 = Color(0xFFF7DAFC)
    val Red400 = Color(0xFFED405B)
    val Red50 = Color(0xFFFDEBEE)
    val Green600 = Color(0xFF18A75A)

    // цвета категорий
    val Blue100 = Color(0xFFB2EBF2)
    val Blue100_2 = Color(0xFFC5CAE9)
    val Blue100_3 = Color(0xFFB3E5FC)
    val Green100 = Color(0xFFC8E6C9)
    val Green100_2 = Color(0xFFDCEDC8)
    val Purple100_3 = Color(0xFFE1BEE7)
    val Purple200 = Color(0xFFB39DDB)
    val Pink100 = Color(0xFFF8BBD0)
    val Red100 = Color(0xFFFFCDD2)
    val Red100_2 = Color(0xFFFFCCBC)
    val Yellow100 = Color(0xFFFFECB3)
    val Yellow100_2 = Color(0xFFFFF9C4)
    val Yellow200 = Color(0xFFE6EE9C)
    val Orange100 = Color(0xFFFFE0B2)
    val Orange100_2 = Color(0xFFFFECB3)
    val Gray100_2 = Color(0xFFCFD8DC)
    val Gray100_3 = Color(0xFFD7CCC8)
}
