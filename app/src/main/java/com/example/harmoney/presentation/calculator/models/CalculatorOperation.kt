package com.example.harmoney.presentation.calculator.models

sealed class CalculatorOperation(val symbol: String) {
    object Divide : CalculatorOperation(symbol = "÷")
    object Multiply : CalculatorOperation(symbol = "x")
    object Subtract : CalculatorOperation(symbol = "-")
    object Add : CalculatorOperation(symbol = "+")
}
