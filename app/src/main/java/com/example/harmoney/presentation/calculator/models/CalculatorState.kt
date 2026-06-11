package com.example.harmoney.presentation.calculator.models

import androidx.compose.runtime.Immutable

@Immutable
data class CalculatorState(
    val equation: String = "",
    val resultString: String = "",
    val result: Double = 0.0,
)
