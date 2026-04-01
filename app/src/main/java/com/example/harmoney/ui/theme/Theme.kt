package com.example.harmoney.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RippleConfiguration
import androidx.compose.material3.RippleDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val LightColorPalette = HarmColors(
    surface = HarmColor.VioletT98,
    onSurface = HarmColor.NeutralT11,
    surfaceContainer = HarmColor.NeutralT89,
    onSurfaceContainer = HarmColor.NeutralT10,
    onSurfaceContainerLow = HarmColor.NeutralT21,
    surfaceVariant = HarmColor.VioletT90,
    onSurfaceVariant = HarmColor.NeutralT30,

    primary = HarmColor.PurpleT43,
    onPrimary = HarmColor.NeutralT100,
    primaryContainer = HarmColor.MagentaT92,
    onPrimaryContainer = HarmColor.PurpleT18,

    secondary = HarmColor.VioletT42,
    onSecondary = HarmColor.NeutralT100,
    secondaryContainer = HarmColor.VioletT91,
    onSecondaryContainer = HarmColor.VioletT13,

    outline = HarmColor.NeutralT50,
    outlineVariant = HarmColor.NeutralT80,

    error = HarmColor.RedT40,
    onError = HarmColor.NeutralT100,
    errorContainer = HarmColor.RedT90,
    onErrorContainer = HarmColor.RedT16,

    info = HarmColor.GreenT90,
    onInfo = HarmColor.GreenT43,

    categoryIconTint = HarmColor.NeutralT0,

    isDark = false
)

private val DarkColorPalette = HarmColors(
    surface = HarmColor.NeutralT11,
    onSurface = HarmColor.NeutralT89,
    surfaceContainer = HarmColor.NeutralT23,
    onSurfaceContainer = HarmColor.NeutralT100,
    onSurfaceContainerLow = HarmColor.NeutralT78,
    surfaceVariant = HarmColor.NeutralT30,
    onSurfaceVariant = HarmColor.NeutralT80,

    primary = HarmColor.VioletT80,
    onPrimary = HarmColor.PurpleT27,
    primaryContainer = HarmColor.PurpleT36,
    onPrimaryContainer = HarmColor.MagentaT92,

    secondary = HarmColor.VioletT81,
    onSecondary = HarmColor.VioletT23,
    secondaryContainer = HarmColor.VioletT32,
    onSecondaryContainer = HarmColor.VioletT91,

    outline = HarmColor.NeutralT60,
    outlineVariant = HarmColor.NeutralT30,

    error = HarmColor.RedT79,
    onError = HarmColor.RedT23,
    errorContainer = HarmColor.RedT30,
    onErrorContainer = HarmColor.RedT90,

    info = HarmColor.GreenT30,
    onInfo = HarmColor.GreenT79,

    categoryIconTint = HarmColor.NeutralT0,

    isDark = true
)

@Composable
fun HarmTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val customColors = if (darkTheme) DarkColorPalette else LightColorPalette

    val typography = Type(
        titleLarge = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 22.sp,
            lineHeight = 28.sp,
            letterSpacing = 0.sp
        ),
        titleLargeSemiBold = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.SemiBold,
            fontSize = 22.sp,
            lineHeight = 28.sp,
            letterSpacing = 0.sp
        ),
        titleMedium = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.sp
        ),
        titleMediumSemiBold = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.sp
        ),
        titleSmall = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.sp
        ),

        bodyLarge = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.sp
        ),
        bodyLargeSemiBold = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.sp
        ),
        bodySmall = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.sp
        ),

        labelLarge = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.sp
        ),
        labelMedium = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.sp
        )
    )

    ProvideTheme(customColors, typography) {
        MaterialTheme(
            colorScheme = debugColors(),
            content = content
        )
    }
}

object HarmTheme {
    val colors: HarmColors
        @Composable
        get() = LocalHarmColors.current

    val typography: Type
        @Composable
        get() = LocalHarmTypography.current
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProvideTheme(
    colors: HarmColors,
    typography: Type,
    content: @Composable () -> Unit,
) {
    val harmTypography = remember { typography }

    val rippleColor = colors.onSurfaceContainer
    val rippleConfig = remember(rippleColor) {
        RippleConfiguration(
            color = rippleColor,
            rippleAlpha = RippleDefaults.RippleAlpha
        )
    }

    CompositionLocalProvider(
        LocalHarmColors provides colors,
        LocalHarmTypography provides harmTypography,
        LocalRippleConfiguration provides rippleConfig,
        content = content
    )
}

private val LocalHarmColors = staticCompositionLocalOf<HarmColors> {
    error("No ColorPalette provided")
}

private val LocalHarmTypography = staticCompositionLocalOf<Type> {
    error("No Typography provided")
}

fun debugColors(
    debugColor: Color = Color.Magenta
) = ColorScheme(
    primary = debugColor,
    onPrimary = debugColor,
    primaryContainer = debugColor,
    onPrimaryContainer = debugColor,
    inversePrimary = debugColor,
    secondary = debugColor,
    onSecondary = debugColor,
    secondaryContainer = debugColor,
    onSecondaryContainer = debugColor,
    tertiary = debugColor,
    onTertiary = debugColor,
    tertiaryContainer = debugColor,
    onTertiaryContainer = debugColor,
    background = debugColor,
    onBackground = debugColor,
    surface = debugColor,
    onSurface = debugColor,
    surfaceVariant = debugColor,
    onSurfaceVariant = debugColor,
    surfaceTint = debugColor,
    inverseSurface = debugColor,
    inverseOnSurface = debugColor,
    error = debugColor,
    onError = debugColor,
    errorContainer = debugColor,
    onErrorContainer = debugColor,
    outline = debugColor,
    outlineVariant = debugColor,
    scrim = debugColor,
    surfaceBright = debugColor,
    surfaceDim = debugColor,
    surfaceContainer = debugColor,
    surfaceContainerHigh = debugColor,
    surfaceContainerHighest = debugColor,
    surfaceContainerLow = debugColor,
    surfaceContainerLowest = debugColor,
)
