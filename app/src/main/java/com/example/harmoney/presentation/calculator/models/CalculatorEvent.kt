package com.example.harmoney.presentation.calculator.models

sealed interface CalculatorEvent {
    data class OnOpenCalculator(val initEquation: String) : CalculatorEvent
    data class OnEnterNumber(val number: String) : CalculatorEvent
    data class OnEnterOperation(val operation: CalculatorOperation) : CalculatorEvent
    data object OnEnterDecimalDot : CalculatorEvent
    data object OnEnterParenthesis : CalculatorEvent
    data object OnClearClick : CalculatorEvent
    data object OnDeleteClick : CalculatorEvent
    data object OnCalculateClick : CalculatorEvent
    data object OnCloseCalculator : CalculatorEvent
}
