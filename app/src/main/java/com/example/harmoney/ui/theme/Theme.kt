package com.example.harmoney.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
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
    surface = HarmColor.Gray50,
    surfaceContainer = HarmColor.Purple50,
    onSurface = HarmColor.Gray900,
    onSurfaceContainer = HarmColor.Gray900,
    onSurfaceContainerLow = HarmColor.Gray600,
    primary = HarmColor.Purple400,
    primaryVariant = HarmColor.Purple100,
    onPrimary = HarmColor.White,
    onPrimaryVariant = HarmColor.Purple400,
    error = HarmColor.Red400,
    errorContainer = HarmColor.Red50,
    info = HarmColor.Green600,
    isDark = false
)

private val DarkColorPalette = HarmColors(
    surface = HarmColor.Gray900,
    surfaceContainer = HarmColor.Gray800,
    onSurface = HarmColor.White,
    onSurfaceContainer = HarmColor.White,
    onSurfaceContainerLow = HarmColor.Gray400,
    primary = HarmColor.Purple800,
    primaryVariant = HarmColor.Purple100,
    onPrimary = HarmColor.White,
    onPrimaryVariant = HarmColor.Purple400,
    error = HarmColor.Red400,
    errorContainer = HarmColor.Red50,
    info = HarmColor.Green600,
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
        titleMedium = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
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

@Composable
fun ProvideTheme(
    colors: HarmColors,
    typography: Type,
    content: @Composable () -> Unit,
) {
    val harmTypography = remember { typography }

    CompositionLocalProvider(
        LocalHarmColors provides colors,
        LocalHarmTypography provides harmTypography,
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
