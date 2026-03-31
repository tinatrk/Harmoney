package com.example.harmoney.ui.theme

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

/**
 * Базовая палитра цветов приложения.
 *
 * Назначения оттенков:
 *
 * **Light theme**
 * - `VioletT98` - surface
 * - `NeutralT11` - on_surface
 * - `NeutralT89` - surface_container
 * - `NeutralT10` - on_surface_container
 * - `NeutralT21` - on_surface_container_low
 * - `VioletT90` - surface_variant
 * - `NeutralT30` - on_surface_variant
 * - `PurpleT43` - primary
 * - `NeutralT100` - on_primary (white)
 * - `MagentaT92` - primary_container
 * - `PurpleT18` - on_primary_container
 * - `VioletT42` - secondary
 * - `NeutralT100` - on_secondary (white)
 * - `VioletT91` - secondary_container
 * - `VioletT13` - on_secondary_container
 * - `NeutralT50` - outline
 * - `NeutralT80` - outlineVariant
 * - `RedT40` - error
 * - `NeutralT100` - on_error (white)
 * - `RedT90` - error_container
 * - `RedT16` - on_error_container
 * - `GreenT90` - info
 * - `GreenT43` - on_info
 *
 * **Dark theme**
 * - `NeutralT11` - surface
 * - `NeutralT89` - on_surface
 * - `NeutralT23` - surface_container
 * - `NeutralT100` - on_surface_container (white)
 * - `NeutralT78` - on_surface_container_low
 * - `NeutralT30` - surface_variant
 * - `NeutralT80` - on_surface_variant
 * - `VioletT80` - primary
 * - `PurpleT27` - on_primary
 * - `PurpleT36` - primary_container
 * - `MagentaT92` - on_primary_container
 * - `VioletT81` - secondary
 * - `VioletT23` - on_secondary
 * - `VioletT32` - secondary_container
 * - `VioletT91` - on_secondary_container
 * - `NeutralT60` - outline
 * - `NeutralT30` - outlineVariant
 * - `RedT79` - error
 * - `RedT23` - on_error
 * - `RedT30` - error_container
 * - `RedT90` - on_error_container
 * - `GreenT30` - info
 * - `GreenT79` - on_info
 *
 * **Common**
 * - `NeutralT0` - (black) вспомогательный цвет (например, для tint иконок категорий)
 *
 * Принцип именования цветов:
 * - hue - название оттенка по цветовому кругу (neutral - оттенки серого)
 * - tone - численный показатель светлости (0 - самый темный, 100 - самый светлый)
 */
@Stable
object HarmColor {
    val VioletT13 = Color(0xFF251A32)
    val VioletT23 = Color(0xFF3B2F48)
    val VioletT32 = Color(0xFF52465F)
    val VioletT42 = Color(0xFF6A5D78)
    val VioletT80 = Color(0xFFE9B6FF)
    val VioletT81 = Color(0xFFD5C1E6)
    val VioletT90 = Color(0xFFEADFED)
    val VioletT91 = Color(0xFFF1DBFF)
    val VioletT98 = Color(0xFFFEF7FF)

    val NeutralT0 = Color(0xFF000000)
    val NeutralT10 = Color(0xFF1C1B1F)
    val NeutralT11 = Color(0xFF1D1A20)
    val NeutralT21 = Color(0xFF322F35)
    val NeutralT23 = Color(0xFF36343A)
    val NeutralT30 = Color(0xFF4B454E)
    val NeutralT50 = Color(0xFF7D757F)
    val NeutralT60 = Color(0xFF968E99)
    val NeutralT78 = Color(0xFFCAC4D0)
    val NeutralT80 = Color(0xFFCEC3D2)
    val NeutralT89 = Color(0xFFE7E0E9)
    val NeutralT100 = Color(0xFFFFFFFF)

    val PurpleT18 = Color(0xFF36004F)
    val PurpleT27 = Color(0xFF530A73)
    val PurpleT36 = Color(0xFF6F1E96)
    val PurpleT43 = Color(0xFF8B22B6)

    val MagentaT92 = Color(0xFFF7DAFC)

    val RedT16 = Color(0xFF410002)
    val RedT23 = Color(0xFF690005)
    val RedT30 = Color(0xFF93000A)
    val RedT40 = Color(0xFFBA1A1A)
    val RedT79 = Color(0xFFFFB4AB)
    val RedT90 = Color(0xFFFFDAD6)

    val GreenT30 = Color(0xFF00522F)
    val GreenT43 = Color(0xFF146C43)
    val GreenT79 = Color(0xFF8FD6A8)
    val GreenT90 = Color(0xFFC4F1D4)
}
