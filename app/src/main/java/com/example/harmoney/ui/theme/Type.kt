package com.example.harmoney.ui.theme

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle

@Stable
class Type(
    titleLarge: TextStyle,
    titleMedium: TextStyle,
    titleSmall: TextStyle,

    bodyLarge: TextStyle,
    bodyMedium: TextStyle,
    bodySmall: TextStyle,

    labelLarge: TextStyle,
    labelMedium: TextStyle
) {
    var titleLarge by mutableStateOf(titleLarge)
        private set
    var titleMedium by mutableStateOf(titleMedium)
        private set
    var titleSmall by mutableStateOf(titleSmall)
        private set

    var bodyLarge by mutableStateOf(bodyLarge)
        private set
    var bodyMedium by mutableStateOf(bodyMedium)
        private set
    var bodySmall by mutableStateOf(bodySmall)
        private set

    var labelLarge by mutableStateOf(labelLarge)
        private set
    var labelMedium by mutableStateOf(labelMedium)
        private set
}
