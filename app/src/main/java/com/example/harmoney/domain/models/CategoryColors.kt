package com.example.harmoney.domain.models

import com.example.harmoney.base.IdEnum
import com.example.harmoney.base.IdEnumRegistry

/** Category colors
 *
 * Принцип именования цветов:
 * - hue - название оттенка по цветовому кругу (neutral - оттенки серого)
 * - tone - численный показатель светлости (0 - самый темный, 100 - самый светлый)
 */
enum class CategoryColors(override val id: Long, val colorValue: Long) : IdEnum {
    RED_T53(id = 1, colorValue = CategoryColor.RED_T53),
    PINK_T58(id = 2, colorValue = CategoryColor.PINK_T58),
    PINK_T65(id = 3, colorValue = CategoryColor.PINK_T65),
    RED_T68(id = 4, colorValue = CategoryColor.RED_T68),
    RED_T77(id = 5, colorValue = CategoryColor.RED_T77),
    PINK_T83(id = 6, colorValue = CategoryColor.PINK_T83),

    ORANGE_T47(id = 7, colorValue = CategoryColor.ORANGE_T47),
    ORANGE_T55(id = 8, colorValue = CategoryColor.ORANGE_T55),
    YELLOW_T58(id = 9, colorValue = CategoryColor.YELLOW_T58),
    ORANGE_T68(id = 10, colorValue = CategoryColor.ORANGE_T68),
    ORANGE_T74(id = 11, colorValue = CategoryColor.ORANGE_T74),
    ORANGE_T78(id = 12, colorValue = CategoryColor.ORANGE_T78),
    YELLOW_T82(id = 13, colorValue = CategoryColor.YELLOW_T82),
    ORANGE_T85(id = 14, colorValue = CategoryColor.ORANGE_T85),
    ORANGE_T90(id = 15, colorValue = CategoryColor.ORANGE_T90),

    GREEN_T53(id = 16, colorValue = CategoryColor.GREEN_T53),
    TEAL_T57(id = 17, colorValue = CategoryColor.TEAL_T57),
    LIME_T62(id = 18, colorValue = CategoryColor.LIME_T62),
    GREEN_T70(id = 19, colorValue = CategoryColor.GREEN_T70),
    GREEN_T88(id = 20, colorValue = CategoryColor.GREEN_T88),
    LIME_T89(id = 21, colorValue = CategoryColor.LIME_T89),

    INDIGO_T60(id = 22, colorValue = CategoryColor.INDIGO_T60),
    BLUE_T60(id = 23, colorValue = CategoryColor.BLUE_T60),
    BLUE_T62(id = 24, colorValue = CategoryColor.BLUE_T62),
    BLUE_T75(id = 25, colorValue = CategoryColor.BLUE_T75),
    INDIGO_T83(id = 26, colorValue = CategoryColor.INDIGO_T83),
    CYAN_T88(id = 27, colorValue = CategoryColor.CYAN_T88),

    MAGENTA_T42(id = 28, colorValue = CategoryColor.MAGENTA_T42),
    PURPLE_T61(id = 29, colorValue = CategoryColor.PURPLE_T61),
    VIOLET_T68(id = 30, colorValue = CategoryColor.VIOLET_T68),
    PURPLE_T71(id = 31, colorValue = CategoryColor.PURPLE_T71),
    PURPLE_T72(id = 32, colorValue = CategoryColor.PURPLE_T72),
    PURPLE_T84(id = 33, colorValue = CategoryColor.PURPLE_T84),

    BROWN_T51(id = 34, colorValue = CategoryColor.BROWN_T51),
    BROWN_T82(id = 35, colorValue = CategoryColor.BROWN_T82),
    BLUE_GRAY_T85(id = 36, colorValue = CategoryColor.BLUE_GRAY_T85);

    companion object {
        private val registry = IdEnumRegistry(CategoryColors::class.java) { it.id }
        fun fromId(id: Long) = registry.fromId(id)
    }
}

private object CategoryColor {
    const val RED_T53 = 0xFFE13F36
    const val PINK_T58 = 0xFFEC407B
    const val PINK_T65 = 0xFFF06292
    const val RED_T68 = 0xFFE57373
    const val RED_T77 = 0xFFEFA0AC
    const val PINK_T83 = 0xFFF5BEDC

    const val ORANGE_T47 = 0xFFC5881D
    const val ORANGE_T55 = 0xFFF0652D
    const val YELLOW_T58 = 0xFFD4B36A
    const val ORANGE_T68 = 0xFFFF8A65
    const val ORANGE_T74 = 0xFFFF9E80
    const val ORANGE_T78 = 0xFFFFB38A
    const val YELLOW_T82 = 0xFFFFCA5F
    const val ORANGE_T85 = 0xFFFFCCBC
    const val ORANGE_T90 = 0xFFFFE0B2

    const val GREEN_T53 = 0xFF348D68
    const val TEAL_T57 = 0xFF009688
    const val LIME_T62 = 0xFF9EBA57
    const val GREEN_T70 = 0xFF5DBB8B
    const val GREEN_T88 = 0xFFC8E6C9
    const val LIME_T89 = 0xFFE6EE9C

    const val INDIGO_T60 = 0xFF7986CB
    const val BLUE_T60 = 0xFF2196F3
    const val BLUE_T62 = 0xFF6F99AD
    const val BLUE_T75 = 0xFF64B5F6
    const val INDIGO_T83 = 0xFFC5CAE9
    const val CYAN_T88 = 0xFFB3E5FC

    const val MAGENTA_T42 = 0xFFB500A4
    const val PURPLE_T61 = 0xFFBA68C8
    const val VIOLET_T68 = 0xFFB085F5
    const val PURPLE_T71 = 0xFFB39DDB
    const val PURPLE_T72 = 0xFFC39BD3
    const val PURPLE_T84 = 0xFFE1BEE7

    const val BROWN_T51 = 0xFFA26B6D
    const val BROWN_T82 = 0xFFD7CCC8
    const val BLUE_GRAY_T85 = 0xFFCFD8DC
}
