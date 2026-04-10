package com.example.harmoney.presentation.calculator.models

sealed class CalculatorAction {
    data class OpenCalculator(val initEquation: String) : CalculatorAction()
    data class Number(val number: String) : CalculatorAction()
    object Clear : CalculatorAction()
    object Delete : CalculatorAction()
    data class Operation(val operation: CalculatorOperation) : CalculatorAction()
    object Calculate : CalculatorAction()
    object Decimal : CalculatorAction()
    object Parenthesis : CalculatorAction()
    object CloseCalculator : CalculatorAction()
}
