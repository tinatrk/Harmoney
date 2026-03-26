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
}
