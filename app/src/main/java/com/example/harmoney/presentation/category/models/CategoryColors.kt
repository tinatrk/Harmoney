package com.example.harmoney.presentation.category.models

import androidx.compose.ui.graphics.Color

/** Category colors
 *
 * Принцип именования цветов:
 * - hue - название оттенка по цветовому кругу (neutral - оттенки серого)
 * - tone - численный показатель светлости (0 - самый темный, 100 - самый светлый)
 */
enum class CategoryColors(val color: Color) {
    RED_T53(color = CategoryColor.RedT53),
    PINK_T58(color = CategoryColor.PinkT58),
    PINK_T65(color = CategoryColor.PinkT65),
    RED_T68(color = CategoryColor.RedT68),
    RED_T77(color = CategoryColor.RedT77),
    PINK_T83(color = CategoryColor.PinkT83),

    ORANGE_T47(color = CategoryColor.OrangeT47),
    ORANGE_T55(color = CategoryColor.OrangeT55),
    YELLOW_T58(color = CategoryColor.YellowT58),
    ORANGE_T68(color = CategoryColor.OrangeT68),
    ORANGE_T74(color = CategoryColor.OrangeT74),
    ORANGE_T78(color = CategoryColor.OrangeT78),
    YELLOW_T82(color = CategoryColor.YellowT82),
    ORANGE_T85(color = CategoryColor.OrangeT85),
    ORANGE_T90(color = CategoryColor.OrangeT90),

    GREEN_T53(color = CategoryColor.GreenT53),
    TEAL_T57(color = CategoryColor.TealT57),
    LIME_T62(color = CategoryColor.LimeT62),
    GREEN_T70(color = CategoryColor.GreenT70),
    GREEN_T88(color = CategoryColor.GreenT88),
    LIME_T89(color = CategoryColor.LimeT89),

    INDIGO_T60(color = CategoryColor.IndigoT60),
    BLUE_T60(color = CategoryColor.BlueT60),
    BLUE_T62(color = CategoryColor.BlueT62),
    BLUE_T75(color = CategoryColor.BlueT75),
    INDIGO_T83(color = CategoryColor.IndigoT83),
    CYAN_T88(color = CategoryColor.CyanT88),

    MAGENTA_T42(color = CategoryColor.MagentaT42),
    PURPLE_T61(color = CategoryColor.PurpleT61),
    VIOLET_T68(color = CategoryColor.VioletT68),
    PURPLE_T71(color = CategoryColor.PurpleT71),
    PURPLE_T72(color = CategoryColor.PurpleT72),
    PURPLE_T84(color = CategoryColor.PurpleT84),

    BROWN_T51(color = CategoryColor.BrownT51),
    BROWN_T82(color = CategoryColor.BrownT82),
    BLUE_GRAY_T85(color = CategoryColor.BlueGrayT85),
}

private object CategoryColor {
    val RedT53 = Color(0xFFE13F36)
    val PinkT58 = Color(0xFFEC407B)
    val PinkT65 = Color(0xFFF06292)
    val RedT68 = Color(0xFFE57373)
    val RedT77 = Color(0xFFEFA0AC)
    val PinkT83 = Color(0xFFF5BEDC)

    val OrangeT47 = Color(0xFFC5881D)
    val OrangeT55 = Color(0xFFF0652D)
    val YellowT58 = Color(0xFFD4B36A)
    val OrangeT68 = Color(0xFFFF8A65)
    val OrangeT74 = Color(0xFFFF9E80)
    val OrangeT78 = Color(0xFFFFB38A)
    val YellowT82 = Color(0xFFFFCA5F)
    val OrangeT85 = Color(0xFFFFCCBC)
    val OrangeT90 = Color(0xFFFFE0B2)

    val GreenT53 = Color(0xFF348D68)
    val TealT57 = Color(0xFF009688)
    val LimeT62 = Color(0xFF9EBA57)
    val GreenT70 = Color(0xFF5DBB8B)
    val GreenT88 = Color(0xFFC8E6C9)
    val LimeT89 = Color(0xFFE6EE9C)

    val IndigoT60 = Color(0xFF7986CB)
    val BlueT60 = Color(0xFF2196F3)
    val BlueT62 = Color(0xFF6F99AD)
    val BlueT75 = Color(0xFF64B5F6)
    val IndigoT83 = Color(0xFFC5CAE9)
    val CyanT88 = Color(0xFFB3E5FC)

    val MagentaT42 = Color(0xFFB500A4)
    val PurpleT61 = Color(0xFFBA68C8)
    val VioletT68 = Color(0xFFB085F5)
    val PurpleT71 = Color(0xFFB39DDB)
    val PurpleT72 = Color(0xFFC39BD3)
    val PurpleT84 = Color(0xFFE1BEE7)

    val BrownT51 = Color(0xFFA26B6D)
    val BrownT82 = Color(0xFFD7CCC8)
    val BlueGrayT85 = Color(0xFFCFD8DC)
}
