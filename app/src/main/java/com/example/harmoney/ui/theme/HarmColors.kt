package com.example.harmoney.ui.theme

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

@Stable
class HarmColors(
    surface: Color,
    surfaceContainer: Color,

    onSurface: Color,
    onSurfaceContainer: Color,
    onSurfaceContainerLow: Color,

    primary: Color,
    primaryVariant: Color,

    onPrimary: Color,
    onPrimaryVariant: Color,

    error: Color,
    errorContainer: Color,

    info: Color,

    isDark: Boolean
) {
    var surface by mutableStateOf(surface)
        private set
    var surfaceContainer by mutableStateOf(surfaceContainer)
        private set
    var onSurface by mutableStateOf(onSurface)
        private set
    var onSurfaceContainer by mutableStateOf(onSurfaceContainer)
        private set
    var onSurfaceContainerLow by mutableStateOf(onSurfaceContainerLow)
        private set
    var primary by mutableStateOf(primary)
        private set
    var primaryVariant by mutableStateOf(primaryVariant)
        private set
    var onPrimary by mutableStateOf(onPrimary)
        private set
    var onPrimaryVariant by mutableStateOf(onPrimaryVariant)
        private set
    var error by mutableStateOf(error)
        private set
    var errorContainer by mutableStateOf(errorContainer)
        private set
    var info by mutableStateOf(info)
        private set
    var isDark by mutableStateOf(isDark)
        private set

    fun update(other: HarmColors) {
        surface = other.surface
        surfaceContainer = other.surfaceContainer
        onSurface = other.onSurface
        onSurfaceContainer = other.onSurfaceContainer
        onSurfaceContainerLow = other.onSurfaceContainerLow
        primary = other.primary
        primaryVariant = other.primaryVariant
        onPrimary = other.onPrimary
        onPrimaryVariant = other.onPrimaryVariant
        error = other.error
        errorContainer = other.errorContainer
        info = other.info
        isDark = other.isDark
    }
}

