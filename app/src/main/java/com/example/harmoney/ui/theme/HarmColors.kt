package com.example.harmoney.ui.theme

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

@Stable
class HarmColors(
    surface: Color,
    onSurface: Color,
    surfaceContainer: Color,
    onSurfaceContainer: Color,
    surfaceContainerLow: Color,
    onSurfaceContainerLow: Color,
    surfaceContainerHigh: Color,
    surfaceContainerHighest: Color,
    surfaceVariant: Color,
    onSurfaceVariant: Color,

    primary: Color,
    onPrimary: Color,
    primaryContainer: Color,
    onPrimaryContainer: Color,

    secondary: Color,
    onSecondary: Color,
    secondaryContainer: Color,
    onSecondaryContainer: Color,

    outline: Color,
    outlineVariant: Color,

    error: Color,
    onError: Color,
    errorContainer: Color,
    onErrorContainer: Color,

    info: Color,
    onInfo: Color,

    borderAndScrim: Color,

    isDark: Boolean
) {
    var surface by mutableStateOf(surface)
        private set
    var onSurface by mutableStateOf(onSurface)
        private set
    var surfaceContainer by mutableStateOf(surfaceContainer)
        private set
    var onSurfaceContainer by mutableStateOf(onSurfaceContainer)
        private set
    var surfaceContainerLow by mutableStateOf(surfaceContainerLow)
        private set
    var onSurfaceContainerLow by mutableStateOf(onSurfaceContainerLow)
        private set
    var surfaceContainerHigh by mutableStateOf(surfaceContainerHigh)
        private set
    var surfaceContainerHighest by mutableStateOf(surfaceContainerHighest)
        private set
    var surfaceVariant by mutableStateOf(surfaceVariant)
        private set
    var onSurfaceVariant by mutableStateOf(onSurfaceVariant)
        private set

    var primary by mutableStateOf(primary)
        private set
    var onPrimary by mutableStateOf(onPrimary)
        private set
    var primaryContainer by mutableStateOf(primaryContainer)
        private set
    var onPrimaryContainer by mutableStateOf(onPrimaryContainer)
        private set

    var secondary by mutableStateOf(secondary)
        private set
    var onSecondary by mutableStateOf(onSecondary)
        private set
    var secondaryContainer by mutableStateOf(secondaryContainer)
        private set
    var onSecondaryContainer by mutableStateOf(onSecondaryContainer)
        private set

    var outline by mutableStateOf(outline)
        private set
    var outlineVariant by mutableStateOf(outlineVariant)
        private set

    var error by mutableStateOf(error)
        private set
    var onError by mutableStateOf(onError)
        private set
    var errorContainer by mutableStateOf(errorContainer)
        private set
    var onErrorContainer by mutableStateOf(onErrorContainer)
        private set

    var info by mutableStateOf(info)
        private set
    var onInfo by mutableStateOf(onInfo)
        private set

    var borderAndScrim by mutableStateOf(borderAndScrim)
        private set

    var isDark by mutableStateOf(isDark)
        private set

    fun update(other: HarmColors) {
        surface = other.surface
        onSurface = other.onSurface
        surfaceContainer = other.surfaceContainer
        onSurfaceContainer = other.onSurfaceContainer
        surfaceContainerLow = other.surfaceContainerLow
        onSurfaceContainerLow = other.onSurfaceContainerLow
        surfaceContainerHigh = other.surfaceContainerHigh
        surfaceContainerHighest = other.surfaceContainerHighest
        surfaceVariant = other.surfaceVariant
        onSurfaceVariant = other.onSurfaceVariant

        primary = other.primary
        onPrimary = other.onPrimary
        primaryContainer = other.primaryContainer
        onPrimaryContainer = other.onPrimaryContainer

        secondary = other.secondary
        onSecondary = other.onSecondary
        secondaryContainer = other.secondaryContainer
        onSecondaryContainer = other.onSecondaryContainer

        outline = other.outline
        outlineVariant = other.outlineVariant

        error = other.error
        onError = other.onError
        errorContainer = other.errorContainer
        onErrorContainer = other.onErrorContainer

        info = other.info
        onInfo = other.onInfo

        borderAndScrim = other.borderAndScrim

        isDark = other.isDark
    }
}
