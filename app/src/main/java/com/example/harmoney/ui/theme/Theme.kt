package com.example.harmoney.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

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


    ProvideTheme(customColors) {
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
}

@Composable
fun ProvideTheme(
    colors: HarmColors,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalHarmColors provides colors,
        content = content
    )
}

private val LocalHarmColors = staticCompositionLocalOf<HarmColors> {
    error("No ColorPalette provided")
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
