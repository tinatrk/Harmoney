package com.example.harmoney.domain.models

import com.example.harmoney.base.IdEnum
import com.example.harmoney.base.IdEnumRegistry

/** Category colors
 *
 * Принцип именования цветов:
 * - hue - название оттенка по цветовому кругу (neutral - оттенки серого)
 * - tone - численный показатель светлости (0 - самый темный, 100 - самый светлый)
 */
enum class CategoryColors(
    override val id: Long,
    val background: Long
) : IdEnum {
    RED_T53(id = 1, background = CategoryColor.RED_T53),
    PINK_T58(id = 2, background = CategoryColor.PINK_T58),
    PINK_T60(id = 3, background = CategoryColor.PINK_T60),
    ROSE_T62(id = 4, background = CategoryColor.ROSE_T62),
    PINK_T65(id = 5, background = CategoryColor.PINK_T65),
    RED_T68(id = 6, background = CategoryColor.RED_T68),
    PINK_T75(id = 7, background = CategoryColor.PINK_T75),

    ORANGE_T55(id = 8, background = CategoryColor.ORANGE_T55),
    CORAL_T60(id = 9, background = CategoryColor.CORAL_T60),
    ORANGE_T60(id = 10, background = CategoryColor.ORANGE_T60),
    ORANGE_T68(id = 11, background = CategoryColor.ORANGE_T68),
    ORANGE_T70(id = 12, background = CategoryColor.ORANGE_T70),

    ORANGE_T47(id = 13, background = CategoryColor.ORANGE_T47),
    AMBER_T56(id = 14, background = CategoryColor.AMBER_T56),
    YELLOW_T58(id = 15, background = CategoryColor.YELLOW_T58),

    OLIVE_T52(id = 16, background = CategoryColor.OLIVE_T52),
    GREEN_T53(id = 17, background = CategoryColor.GREEN_T53),
    CYAN_T55(id = 18, background = CategoryColor.CYAN_T55),
    TEAL_T57(id = 19, background = CategoryColor.TEAL_T57),
    MINT_T58(id = 20, background = CategoryColor.MINT_T58),
    GREEN_T60(id = 21, background = CategoryColor.GREEN_T60),
    LIME_T62(id = 22, background = CategoryColor.LIME_T62),
    GREEN_T70(id = 23, background = CategoryColor.GREEN_T70),
    TEAL_T75(id = 24, background = CategoryColor.TEAL_T75),

    COBALT_T56(id = 25, background = CategoryColor.COBALT_T56),
    INDIGO_T60(id = 26, background = CategoryColor.INDIGO_T60),
    BLUE_T60(id = 27, background = CategoryColor.BLUE_T60),
    BLUE_T62(id = 28, background = CategoryColor.BLUE_T62),
    AQUA_T62(id = 29, background = CategoryColor.AQUA_T62),
    SKY_T63(id = 30, background = CategoryColor.SKY_T63),
    BLUE_T75(id = 31, background = CategoryColor.BLUE_T75),
    BLUE_T80(id = 32, background = CategoryColor.BLUE_T80),

    PURPLE_T61(id = 33, background = CategoryColor.PURPLE_T61),
    VIOLET_T68(id = 34, background = CategoryColor.VIOLET_T68),
    PURPLE_T71(id = 35, background = CategoryColor.PURPLE_T71),
    PURPLE_T72(id = 36, background = CategoryColor.PURPLE_T72),

    BROWN_T51(id = 37, background = CategoryColor.BROWN_T51),
    SLATE_T55(id = 38, background = CategoryColor.SLATE_T55);

    companion object {
        private val registry = IdEnumRegistry(CategoryColors::class.java) { it.id }
        fun fromId(id: Long) = registry.fromId(id)
    }
}

private object CategoryColor {

    const val RED_T53 = 0xFFE13F36
    const val PINK_T58 = 0xFFEC407B
    const val PINK_T60 = 0xFFE91E63
    const val ROSE_T62 = 0xFFE85D75
    const val PINK_T65 = 0xFFF06292
    const val RED_T68 = 0xFFE57373
    const val PINK_T75 = 0xFFF48FB1

    const val ORANGE_T55 = 0xFFF0652D
    const val CORAL_T60 = 0xFFE76F51
    const val ORANGE_T60 = 0xFFFF9800
    const val ORANGE_T68 = 0xFFFF8A65
    const val ORANGE_T70 = 0xFFFFB74D

    const val ORANGE_T47 = 0xFFC5881D
    const val AMBER_T56 = 0xFFD99A28
    const val YELLOW_T58 = 0xFFD4B36A

    const val OLIVE_T52 = 0xFF8FA34A
    const val GREEN_T53 = 0xFF348D68
    const val CYAN_T55 = 0xFF0097A7
    const val TEAL_T57 = 0xFF009688
    const val MINT_T58 = 0xFF2EAA8A
    const val GREEN_T60 = 0xFF4CAF50
    const val LIME_T62 = 0xFF9EBA57
    const val GREEN_T70 = 0xFF5DBB8B
    const val TEAL_T75 = 0xFF80CBC4

    const val COBALT_T56 = 0xFF4E74C8
    const val INDIGO_T60 = 0xFF7986CB
    const val BLUE_T60 = 0xFF2196F3
    const val BLUE_T62 = 0xFF6F99AD
    const val AQUA_T62 = 0xFF4FB6C2
    const val SKY_T63 = 0xFF4AA3DF
    const val BLUE_T75 = 0xFF64B5F6
    const val BLUE_T80 = 0xFF9FA8DA

    const val PURPLE_T61 = 0xFFBA68C8
    const val VIOLET_T68 = 0xFFB085F5
    const val PURPLE_T71 = 0xFFB39DDB
    const val PURPLE_T72 = 0xFFC39BD3

    const val BROWN_T51 = 0xFFA26B6D
    const val SLATE_T55 = 0xFF6F7F8F
}
