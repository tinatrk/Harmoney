package com.example.harmoney.presentation.calculator.models

sealed class CalculatorOperation(val symbol: String) {
    object Divide : CalculatorOperation(symbol = CalculatorSymbol.DIVIDE.symbol)
    object Multiply : CalculatorOperation(symbol = CalculatorSymbol.MULTIPLY.symbol)
    object Subtract : CalculatorOperation(symbol = CalculatorSymbol.MINUS.symbol)
    object Add : CalculatorOperation(symbol = CalculatorSymbol.PLUS.symbol)
}
