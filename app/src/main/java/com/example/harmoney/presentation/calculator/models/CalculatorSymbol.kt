package com.example.harmoney.presentation.calculator.models

enum class CalculatorSymbol(val symbol: String, val type: CalculatorSymbolType) {
    ZERO(symbol = Symbols.ZERO_SYMBOL, type = CalculatorSymbolType.NUMBER),
    ONE(symbol = Symbols.ONE_SYMBOL, type = CalculatorSymbolType.NUMBER),
    TWO(symbol = Symbols.TWO_SYMBOL, type = CalculatorSymbolType.NUMBER),
    THREE(symbol = Symbols.THREE_SYMBOL, type = CalculatorSymbolType.NUMBER),
    FOUR(symbol = Symbols.FOUR_SYMBOL, type = CalculatorSymbolType.NUMBER),
    FIVE(symbol = Symbols.FIVE_SYMBOL, type = CalculatorSymbolType.NUMBER),
    SIX(symbol = Symbols.SIX_SYMBOL, type = CalculatorSymbolType.NUMBER),
    SEVEN(symbol = Symbols.SEVEN_SYMBOL, type = CalculatorSymbolType.NUMBER),
    EIGHT(symbol = Symbols.EIGHT_SYMBOL, type = CalculatorSymbolType.NUMBER),
    NINE(symbol = Symbols.NINE_SYMBOL, type = CalculatorSymbolType.NUMBER),

    PLUS(symbol = Symbols.PLUS_SYMBOL, type = CalculatorSymbolType.OPERATION),
    MINUS(symbol = Symbols.MINUS_SYMBOL, type = CalculatorSymbolType.OPERATION),
    MULTIPLY(symbol = Symbols.MULTIPLY_SYMBOL, type = CalculatorSymbolType.OPERATION),
    DIVIDE(symbol = Symbols.DIVIDE_SYMBOL, type = CalculatorSymbolType.OPERATION),

    DOT(symbol = Symbols.DOT_SYMBOL, type = CalculatorSymbolType.DOT),
    PARENTHESIS_OPEN(
        symbol = Symbols.OPEN_PARENTHESIS_SYMBOL,
        type = CalculatorSymbolType.PARENTHESIS_OPEN
    ),
    PARENTHESIS_CLOSED(
        symbol = Symbols.CLOSED_PARENTHESIS_SYMBOL,
        type = CalculatorSymbolType.PARENTHESIS_CLOSED
    ),

    CLEAR(symbol = Symbols.CLEAR_SYMBOL, type = CalculatorSymbolType.NOTHING),
    DELETE_LAST(symbol = Symbols.DELETE_LAST_SYMBOL, type = CalculatorSymbolType.NOTHING),
    EQUALITY(symbol = Symbols.EQUALITY_SYMBOL, type = CalculatorSymbolType.NOTHING),
    NOTHING(symbol = Symbols.EMPTY_STRING, type = CalculatorSymbolType.NOTHING);

    companion object {
        fun getTypeFromString(symbol: String): CalculatorSymbolType {
            val res = CalculatorSymbol.entries.firstOrNull() { it.symbol == symbol }
            return res?.type ?: CalculatorSymbolType.NOTHING
        }

        fun getNumbers(): List<CalculatorSymbol> {
            return CalculatorSymbol.entries.filter { it.type == CalculatorSymbolType.NUMBER }
        }

        fun getOperations(): List<CalculatorSymbol> {
            return CalculatorSymbol.entries.filter { it.type == CalculatorSymbolType.OPERATION }
        }

        fun getMultiplyMathSymbol(): String = Symbols.MULTIPLY_SYMBOL_MATH
        fun getDivideMathSymbol(): String = Symbols.DIVIDE_SYMBOL_MATH
    }
}

enum class CalculatorSymbolType {
    NUMBER,
    OPERATION,
    DOT,
    PARENTHESIS_OPEN,
    PARENTHESIS_CLOSED,
    NOTHING, // символ такого типа не отображается в математическом выражении
}

private object Symbols {
    const val EMPTY_STRING = ""
    const val OPEN_PARENTHESIS_SYMBOL = "("
    const val CLOSED_PARENTHESIS_SYMBOL = ")"
    const val DOT_SYMBOL = "."
    const val ZERO_SYMBOL = "0"
    const val ONE_SYMBOL = "1"
    const val TWO_SYMBOL = "2"
    const val THREE_SYMBOL = "3"
    const val FOUR_SYMBOL = "4"
    const val FIVE_SYMBOL = "5"
    const val SIX_SYMBOL = "6"
    const val SEVEN_SYMBOL = "7"
    const val EIGHT_SYMBOL = "8"
    const val NINE_SYMBOL = "9"
    const val PLUS_SYMBOL = "+"
    const val MINUS_SYMBOL = "-"
    const val MULTIPLY_SYMBOL = "x"
    const val DIVIDE_SYMBOL = "÷"
    const val MULTIPLY_SYMBOL_MATH = "*"
    const val DIVIDE_SYMBOL_MATH = "/"
    const val EQUALITY_SYMBOL = "="
    const val CLEAR_SYMBOL = "C"
    const val DELETE_LAST_SYMBOL = "Del"
}
