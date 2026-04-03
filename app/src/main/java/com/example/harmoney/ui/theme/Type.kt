package com.example.harmoney.ui.theme

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle

@Stable
class Type(
    titleLarge: TextStyle,
    titleLargeSemiBold: TextStyle,
    titleMedium: TextStyle,
    titleMediumSemiBold: TextStyle,
    titleSmall: TextStyle,
    titleSmallSemiBold: TextStyle,

    bodyLarge: TextStyle,
    bodyLargeSemiBold: TextStyle,
    bodyMedium: TextStyle,
    bodySmall: TextStyle,

    labelLarge: TextStyle,
    labelMedium: TextStyle
) {
    var titleLarge by mutableStateOf(titleLarge)
        private set
    var titleLargeSemiBold by mutableStateOf(titleLargeSemiBold)
        private set
    var titleMedium by mutableStateOf(titleMedium)
        private set
    var titleMediumSemiBold by mutableStateOf(titleMediumSemiBold)
        private set
    var titleSmall by mutableStateOf(titleSmall)
        private set
    var titleSmallSemiBold by mutableStateOf(titleSmallSemiBold)
        private set

    var bodyLarge by mutableStateOf(bodyLarge)
        private set
    var bodyLargeSemiBold by mutableStateOf(bodyLargeSemiBold)
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
