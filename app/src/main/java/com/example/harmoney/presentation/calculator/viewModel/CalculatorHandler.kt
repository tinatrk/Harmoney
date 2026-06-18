package com.example.harmoney.presentation.calculator.viewModel

import android.util.Log
import com.example.harmoney.presentation.calculator.models.CalculatorOperation
import com.example.harmoney.presentation.calculator.models.CalculatorResult
import com.example.harmoney.presentation.calculator.models.CalculatorSymbol
import com.example.harmoney.presentation.calculator.models.CalculatorSymbolType
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder
import java.math.RoundingMode

class CalculatorHandler {
    private val numbers: MutableList<String> = mutableListOf()
    private val operations: MutableList<String> = mutableListOf()
    private val results: MutableList<CalculatorResult> = mutableListOf()

    private var countOpenParenthesis = 0
    private var countClosedParenthesis = 0
    private var lastSymbolType = CalculatorSymbolType.NOTHING

    fun initCalculator(equation: String): String {
        var newEquation = EMPTY_STRING
        clearData()
        val resArray = equation.toCharArray().filter { it != ' ' }
        resArray.forEach { symbol ->
            newEquation = if (symbol.toString() == CalculatorSymbol.MINUS.symbol)
                processOperationInput(
                    equation = newEquation,
                    operation = CalculatorOperation.Subtract
                )
            else {
                processNumberInput(equation = newEquation, symbol = symbol.toString())
            }
        }
        return newEquation
    }

    fun getResult(isNeedProcessing: Boolean = false): CalculatorResult {
        var result = if (results.isNotEmpty()) {
            results.last()
        } else {
            CalculatorResult(resultNumeric = ZERO_RESULT, resultString = EMPTY_STRING)
        }
        if (isNeedProcessing) {
            result = processResult(result.resultNumeric)
        }
        return result
    }

    fun processNumberInput(equation: String, symbol: String): String {
        val newEquation = when (lastSymbolType) {

            CalculatorSymbolType.NUMBER, CalculatorSymbolType.DOT -> {
                val lastNumber = numbers.last().trim()
                // если введен 0 и пытаемся написать еще число, то заменяем 0 на это число
                if (lastNumber == CalculatorSymbol.ZERO.symbol
                    && symbol != CalculatorSymbol.DOT.symbol
                ) {
                    replaceLastElement(
                        equation = equation,
                        element = symbol,
                        symbolType = CalculatorSymbolType.NUMBER
                    )
                } else {
                    addDigit(
                        equation = equation,
                        symbol = symbol,
                        symbolType = CalculatorSymbolType.NUMBER
                    )
                }
            }

            CalculatorSymbolType.PARENTHESIS_CLOSED -> {
                addElement(
                    equation = equation,
                    element = symbol,
                    symbolType = CalculatorSymbolType.NUMBER,
                    isNeedMultiply = true
                )
            }

            else -> {
                addElement(
                    equation = equation,
                    element = symbol,
                    symbolType = CalculatorSymbolType.NUMBER
                )
            }
        }

        lastSymbolType = CalculatorSymbolType.NUMBER
        results.add(calculate(newEquation))
        return newEquation
    }

    fun clearData() {
        numbers.clear()
        operations.clear()
        results.clear()
        countOpenParenthesis = 0
        countClosedParenthesis = 0
        lastSymbolType = CalculatorSymbolType.NOTHING
    }

    fun processDeleting(equation: String): String {
        return if (numbers.isNotEmpty() || countOpenParenthesis > 0) {
            val equation: String = when (lastSymbolType) {
                CalculatorSymbolType.OPERATION -> {
                    operations.removeAt(operations.lastIndex)
                    equation.dropLast(2) // удаляем оператор и разделитель
                }

                CalculatorSymbolType.PARENTHESIS_OPEN -> {
                    countOpenParenthesis--
                    equation.dropLast(2) // удаляем скобку и разделитель
                }

                CalculatorSymbolType.PARENTHESIS_CLOSED -> {
                    countClosedParenthesis--
                    equation.dropLast(2) // удаляем скобку и разделитель
                }

                CalculatorSymbolType.NUMBER, CalculatorSymbolType.DOT -> {
                    val lastNumber = numbers.last()
                    numbers.removeAt(numbers.lastIndex)
                    // если удаляем разряд числа (или точку)
                    if (lastNumber.length >= 2) {
                        numbers.add(lastNumber.dropLast(1))
                        equation.dropLast(1) // удаляем разряд
                    } else {
                        equation.dropLast(2) // удаляем число и разделитель
                    }
                }

                CalculatorSymbolType.NOTHING -> {
                    equation
                }
            }

            val lastSymbol = if (equation.isNotEmpty()) equation.last().toString() else EMPTY_STRING
            lastSymbolType = CalculatorSymbol.getTypeFromString(lastSymbol)

            results.removeAt(results.lastIndex)
            equation
        } else { // удаляем минус в начале выражения
            if (equation.trim() == CalculatorOperation.Subtract.symbol && operations.size == 1) {
                operations.removeAt(operations.lastIndex)
                results.removeAt(results.lastIndex)
                equation.dropLast(2) // удаляем оператор и разделитель
            } else {
                equation
            }
        }
    }

    fun processOperationInput(equation: String, operation: CalculatorOperation): String {
        var isLastSymbolOperation = true
        return if (numbers.isNotEmpty() || countOpenParenthesis > 0) {
            val newEquation: String = when (lastSymbolType) {

                CalculatorSymbolType.NUMBER, CalculatorSymbolType.DOT,
                CalculatorSymbolType.PARENTHESIS_CLOSED -> {
                    addElement(
                        equation = equation,
                        element = operation.symbol,
                        symbolType = CalculatorSymbolType.OPERATION,
                    )
                }

                CalculatorSymbolType.PARENTHESIS_OPEN, CalculatorSymbolType.NOTHING -> {
                    if (operation is CalculatorOperation.Subtract) {
                        addElement(
                            equation = equation,
                            element = operation.symbol,
                            symbolType = CalculatorSymbolType.OPERATION
                        )
                    } else {
                        isLastSymbolOperation = false
                        equation
                    }
                }

                CalculatorSymbolType.OPERATION -> {
                    replaceLastElement(
                        equation = equation,
                        element = operation.symbol,
                        symbolType = CalculatorSymbolType.OPERATION
                    )
                }
            }

            if (isLastSymbolOperation) lastSymbolType = CalculatorSymbolType.OPERATION

            results.add(calculate(newEquation))
            newEquation
        } else { // пишем минус в начале выражения
            if (operation is CalculatorOperation.Subtract && operations.isEmpty()) {
                val newEquation = addElement(
                    equation = equation,
                    element = operation.symbol,
                    symbolType = CalculatorSymbolType.OPERATION
                )
                results.add(calculate(newEquation))
                newEquation
            } else {
                //если пытаемся написать в начале выражение оператор не минус, то ничего не делаем
                equation
            }
        }
    }

    fun processDecimalInput(equation: String): String {
        var newEquation = equation
        when (lastSymbolType) {
            CalculatorSymbolType.NUMBER -> {
                val lastNumber =
                    if (numbers.isNotEmpty()) numbers.last() else EMPTY_STRING
                if (!lastNumber.contains(CalculatorSymbol.DOT.symbol)) {
                    newEquation = processNumberInput(
                        equation = equation,
                        symbol = CalculatorSymbol.DOT.symbol
                    )
                }
            }

            CalculatorSymbolType.DOT -> {}

            else -> {
                val timeEquation = processNumberInput(
                    equation = equation,
                    symbol = CalculatorSymbol.ZERO.symbol
                )
                newEquation = processNumberInput(
                    equation = timeEquation,
                    symbol = CalculatorSymbol.DOT.symbol
                )
            }
        }

        lastSymbolType = CalculatorSymbolType.DOT
        return newEquation
    }

    fun processParenthesisInput(equation: String): String {
        var isLastSymbolOpenParenthesis = true
        val newEquation: String = when (lastSymbolType) {
            CalculatorSymbolType.NOTHING, CalculatorSymbolType.OPERATION,
            CalculatorSymbolType.PARENTHESIS_OPEN -> {
                isLastSymbolOpenParenthesis = true
                addElement(
                    equation = equation,
                    element = CalculatorSymbol.PARENTHESIS_OPEN.symbol,
                    symbolType = CalculatorSymbolType.PARENTHESIS_OPEN,
                )
            }

            CalculatorSymbolType.NUMBER, CalculatorSymbolType.DOT,
            CalculatorSymbolType.PARENTHESIS_CLOSED -> {
                if (countOpenParenthesis > countClosedParenthesis) {
                    isLastSymbolOpenParenthesis = false
                    addElement(
                        equation = equation,
                        element = CalculatorSymbol.PARENTHESIS_CLOSED.symbol,
                        symbolType = CalculatorSymbolType.PARENTHESIS_CLOSED,
                    )
                } else {
                    isLastSymbolOpenParenthesis = true
                    addElement(
                        equation = equation,
                        element = CalculatorSymbol.PARENTHESIS_OPEN.symbol,
                        symbolType = CalculatorSymbolType.PARENTHESIS_OPEN,
                        isNeedMultiply = true
                    )
                }
            }
        }
        lastSymbolType = if (isLastSymbolOpenParenthesis) {
            CalculatorSymbolType.PARENTHESIS_OPEN
        } else {
            CalculatorSymbolType.PARENTHESIS_CLOSED
        }

        results.add(calculate(newEquation))
        return newEquation
    }

    // добавить оператор, скобку или первую цифру числа
    private fun addElement(
        equation: String,
        element: String,
        symbolType: CalculatorSymbolType,
        isNeedMultiply: Boolean = false
    ): String {
        // Еcли перед элементом нужно вставить умножение
        val newEquation = if (isNeedMultiply) {
            processOperationInput(
                equation = equation,
                operation = CalculatorOperation.Multiply
            )
        } else equation

        when (symbolType) {
            CalculatorSymbolType.NUMBER -> numbers.add(element)
            CalculatorSymbolType.OPERATION -> operations.add(element)
            CalculatorSymbolType.PARENTHESIS_OPEN -> countOpenParenthesis++
            CalculatorSymbolType.PARENTHESIS_CLOSED -> countClosedParenthesis++
            else -> {}
        }

        return newEquation + DELIMITER + element
    }

    // добавить к числу разряд или точку
    private fun addDigit(
        equation: String,
        symbol: String,
        symbolType: CalculatorSymbolType
    ): String {
        if (symbolType != CalculatorSymbolType.NUMBER || numbers.isEmpty()) return equation

        numbers[numbers.lastIndex] = numbers.last() + symbol
        return equation + symbol
    }

    // заменить оператор или цифру (не разряд числа)
    private fun replaceLastElement(
        equation: String,
        element: String,
        symbolType: CalculatorSymbolType
    ): String {
        // в текущей реализации подменются только числа и операторы, поэтому другие типы не
        // обрабатываются
        if (symbolType != CalculatorSymbolType.NUMBER
            && symbolType != CalculatorSymbolType.OPERATION
        ) return equation

        // Размер results равен длине выражения (без разделителей). После сalculate всегда
        // делаем results.add(), поэтому, если длина выражения не меняется (когда делаем замену),
        // нужно вручную удалить последний result.
        results.removeAt(results.lastIndex)

        when (symbolType) {
            CalculatorSymbolType.NUMBER -> {
                if (numbers.isNotEmpty()) numbers[numbers.lastIndex] = element
            }

            CalculatorSymbolType.OPERATION -> {
                if (operations.isNotEmpty()) operations[operations.lastIndex] = element
            }

            else -> {}
        }

        return equation.dropLast(1) + element
    }

    fun calculate(equation: String): CalculatorResult {
        val correctedEquation = correctEquation(equation)

        val res = try {
            val expr: Expression = ExpressionBuilder(correctedEquation)
                .build()
            val resultNumeric = expr.evaluate()
            processResult(resultNumeric)
        } catch (e: IllegalArgumentException) {
            e.message?.let { Log.e(HARM_APP_TAG, it) }
            CalculatorResult(resultNumeric = ZERO_RESULT, resultString = EMPTY_STRING)
        }

        return res
    }

    private fun correctEquation(equation: String): String {
        var completedEquation = equation
        var localCountOpenParenthesis = countOpenParenthesis
        var localCountClosedParenthesis = countClosedParenthesis

        // удаляем в конце выражения операторы и открытые скобки
        var isEquationCorrect = false
        while (!isEquationCorrect) {
            val lastSymbol = if (completedEquation.isNotEmpty()) {
                completedEquation.last().toString()
            } else {
                EMPTY_STRING
            }
            val symbolType = CalculatorSymbol.getTypeFromString(lastSymbol)

            completedEquation = when (symbolType) {
                CalculatorSymbolType.OPERATION -> {
                    isEquationCorrect = false
                    completedEquation.dropLast(2) // оператор и разделитель
                }

                CalculatorSymbolType.PARENTHESIS_OPEN -> {
                    isEquationCorrect = false
                    localCountOpenParenthesis--
                    completedEquation.dropLast(2) // оператор и разделитель
                }

                else -> {
                    isEquationCorrect = true
                    completedEquation
                }
            }
        }
        // закрываем скобки
        while (localCountOpenParenthesis > localCountClosedParenthesis) {
            completedEquation += DELIMITER + CalculatorSymbol.PARENTHESIS_CLOSED.symbol
            localCountClosedParenthesis++
        }

        //Заменяем x на *
        completedEquation =
            completedEquation.replace(
                oldValue = CalculatorSymbol.MULTIPLY.symbol,
                newValue = CalculatorSymbol.getMultiplyMathSymbol()
            )
        //Заменяем ÷ на /
        completedEquation =
            completedEquation.replace(
                oldValue = CalculatorSymbol.DIVIDE.symbol,
                newValue = CalculatorSymbol.getDivideMathSymbol()
            )

        return completedEquation
    }

    private fun processResult(result: Double): CalculatorResult {
        val longres = result.toLong()
        val digits = if (longres.toDouble() == result) MIN_DECIMAL_PLACES else MAX_DECIMAL_PLACES

        val rounded = result.toBigDecimal().setScale(digits, RoundingMode.HALF_UP)

        return CalculatorResult(rounded.toDouble(), rounded.toString())
    }

    private companion object {
        const val HARM_APP_TAG = "CalculatorHandler"
        const val EMPTY_STRING = ""
        const val DELIMITER = " "
        const val ZERO_RESULT = 0.0
        const val MIN_DECIMAL_PLACES = 0
        const val MAX_DECIMAL_PLACES = 10
    }
}
