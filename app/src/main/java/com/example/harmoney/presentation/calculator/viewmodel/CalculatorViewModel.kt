package com.example.harmoney.presentation.calculator.viewmodel

import androidx.lifecycle.ViewModel
import com.example.harmoney.presentation.calculator.models.CalculatorAction
import com.example.harmoney.presentation.calculator.models.CalculatorOperation
import com.example.harmoney.presentation.calculator.models.CalculatorState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder
import android.util.Log

class CalculatorViewModel : ViewModel() {
    private val _calculatorStateFlow = MutableStateFlow(CalculatorState())
    val calculatorStateFlow = _calculatorStateFlow.asStateFlow()

    private val numbers: MutableList<String> = mutableListOf()
    private val operations: MutableList<String> = mutableListOf()
    private val results: MutableList<String> = mutableListOf()

    private var countOpenParenthesis = 0
    private var countClosedParenthesis = 0
    private var lastSymbolType = SymbolType.NOTHING


    fun onAction(action: CalculatorAction) {
        when (action) {
            is CalculatorAction.OpenCalculator -> initializeEquation(action.initEquation)
            is CalculatorAction.Number -> enterNumber(action.number)
            is CalculatorAction.Clear -> clear()
            is CalculatorAction.Delete -> delete()
            is CalculatorAction.Operation -> enterOperation(action.operation)
            is CalculatorAction.Decimal -> enterDecimal()
            is CalculatorAction.Calculate -> showResult()
            is CalculatorAction.Parenthesis -> enterParenthesis()
            is CalculatorAction.CloseCalculator -> clearData()
        }
    }

    private fun changeState(equation: String, result: String) {
        _calculatorStateFlow.value = _calculatorStateFlow.value.copy(
            equation = equation,
            result = result
        )
    }

    private fun initializeEquation(initEquation: String) {
        val equation = addResultList(initEquation)
        val result = if (results.isNotEmpty()) results.last() else EMPTY_STRING
        changeState(equation = equation, result = result)
    }

    private fun enterNumber(symbol: String) {
        val equation =
            processNumberInput(equation = _calculatorStateFlow.value.equation, symbol = symbol)
        val result = if (results.isNotEmpty()) results.last() else EMPTY_STRING
        changeState(equation, result)
    }

    private fun processNumberInput(equation: String, symbol: String): String {
        val newEquation = when (lastSymbolType) {

            SymbolType.NUMBER, SymbolType.DOT -> {
                val lastNumber = numbers.last().trim()
                // если введен 0 и пытаемся написать еще число, то заменяем 0 на это число
                if (lastNumber == ZERO_SYMBOL && symbol != DOT_SYMBOL) {
                    replaceLastElement(equation = equation, element = symbol, SymbolType.NUMBER)
                } else {
                    addDigit(equation = equation, symbol = symbol, symbolType = SymbolType.NUMBER)
                }
            }

            SymbolType.PARENTHESIS_CLOSED -> {
                addElement(
                    equation = equation,
                    element = symbol,
                    symbolType = SymbolType.NUMBER,
                    isNeedMultiply = true
                )
            }

            else -> {
                addElement(
                    equation = equation,
                    element = symbol,
                    symbolType = SymbolType.NUMBER
                )
            }
        }

        lastSymbolType = SymbolType.NUMBER
        results.add(calculate(newEquation))
        return newEquation
    }

    private fun clear() {
        clearData()
        changeState(equation = EMPTY_STRING, result = EMPTY_STRING)
    }

    private fun clearData() {
        numbers.clear()
        operations.clear()
        results.clear()
        countOpenParenthesis = 0
        countClosedParenthesis = 0
        lastSymbolType = SymbolType.NOTHING
    }

    private fun delete() {
        val equation = processDeleting(equation = _calculatorStateFlow.value.equation)
        val result = if (results.isNotEmpty()) results.last() else EMPTY_STRING
        changeState(equation, result)
    }

    private fun processDeleting(equation: String): String {
        return if (numbers.isNotEmpty() || countOpenParenthesis > 0) {
            val equation: String = when (lastSymbolType) {
                SymbolType.OPERATION -> {
                    operations.removeLast()
                    equation.dropLast(2) // удаляем оператор и разделитель
                }

                SymbolType.PARENTHESIS_OPEN -> {
                    countOpenParenthesis--
                    equation.dropLast(2) // удаляем скобку и разделитель
                }

                SymbolType.PARENTHESIS_CLOSED -> {
                    countClosedParenthesis--
                    equation.dropLast(2) // удаляем скобку и разделитель
                }

                SymbolType.NUMBER, SymbolType.DOT -> {
                    val lastNumber = numbers.last()
                    numbers.removeLast()
                    // если удаляем разряд числа (или точку)
                    if (lastNumber.length >= 2) {
                        numbers.add(lastNumber.dropLast(1))
                        equation.dropLast(1) // удаляем разряд
                    } else {
                        equation.dropLast(2) // удаляем число и разделитель
                    }
                }

                SymbolType.NOTHING -> {
                    equation
                }
            }

            val lastSymbol = if (equation.isNotEmpty()) equation.last().toString() else EMPTY_STRING

            lastSymbolType = getLastSymbolType(lastSymbol)

            results.removeLast()
            equation
        } else { // удаляем минус в начале выражения
            if (equation.trim() == CalculatorOperation.Subtract.symbol && operations.size == 1) {
                operations.removeLast()
                results.removeLast()
                equation.dropLast(2) // удаляем оператор и разделитель
            } else {
                equation
            }
        }
    }

    private fun enterOperation(operation: CalculatorOperation) {
        val equation = processOperationInput(
            equation = _calculatorStateFlow.value.equation,
            operation = operation
        )
        val result = if (results.isNotEmpty()) results.last() else EMPTY_STRING
        changeState(equation, result)
    }

    private fun processOperationInput(equation: String, operation: CalculatorOperation): String {
        var isLastSymbolOperation = true
        return if (numbers.isNotEmpty() || countOpenParenthesis > 0) {
            val equation: String = when (lastSymbolType) {

                SymbolType.NUMBER, SymbolType.DOT, SymbolType.PARENTHESIS_CLOSED -> {
                    addElement(
                        equation = equation,
                        element = operation.symbol,
                        symbolType = SymbolType.OPERATION,
                    )
                }

                SymbolType.PARENTHESIS_OPEN, SymbolType.NOTHING -> {
                    if (operation is CalculatorOperation.Subtract) {
                        addElement(
                            equation = equation,
                            element = operation.symbol,
                            symbolType = SymbolType.OPERATION
                        )
                    } else {
                        isLastSymbolOperation = false
                        equation
                    }
                }

                SymbolType.OPERATION -> {
                    replaceLastElement(
                        equation = equation,
                        element = operation.symbol,
                        SymbolType.OPERATION
                    )
                }
            }

            if (isLastSymbolOperation) lastSymbolType = SymbolType.OPERATION

            results.add(calculate(equation))
            equation
        } else { // пишем минус в начале выражения
            if (operation is CalculatorOperation.Subtract && operations.isEmpty()) {
                val newEducation = addElement(
                    equation = equation,
                    element = operation.symbol,
                    symbolType = SymbolType.OPERATION
                )
                results.add(calculate(equation))
                newEducation
            } else {
                equation
            }
        }
    }

    private fun enterDecimal() {
        val equation = processDecimalInput(equation = _calculatorStateFlow.value.equation)
        val result = if (results.isNotEmpty()) results.last() else EMPTY_STRING
        changeState(equation, result)
    }

    private fun processDecimalInput(equation: String): String {
        var newEquation = equation
        when (lastSymbolType) {
            SymbolType.NUMBER -> {
                val lastNumber = if (numbers.isNotEmpty()) numbers.last() else EMPTY_STRING
                if (!lastNumber.contains(DOT_SYMBOL)) {
                    newEquation = processNumberInput(equation = equation, symbol = DOT_SYMBOL)
                }
            }

            SymbolType.DOT -> {}

            else -> {
                val timeEquation = processNumberInput(equation = equation, symbol = ZERO_SYMBOL)
                newEquation = processNumberInput(equation = timeEquation, symbol = DOT_SYMBOL)
            }
        }

        lastSymbolType = SymbolType.DOT
        return newEquation
    }

    private fun enterParenthesis() {
        val equation = processParenthesisInput(equation = _calculatorStateFlow.value.equation)
        val result = if (results.isNotEmpty()) results.last() else EMPTY_STRING
        changeState(equation, result)
    }

    private fun processParenthesisInput(equation: String): String {
        var isLastSymbolOpenParenthesis = true
        val newEquation: String = when (lastSymbolType) {
            SymbolType.NOTHING, SymbolType.OPERATION, SymbolType.PARENTHESIS_OPEN -> {
                isLastSymbolOpenParenthesis = true
                addElement(
                    equation = equation,
                    element = OPEN_PARENTHESIS_SYMBOL,
                    symbolType = SymbolType.PARENTHESIS_OPEN,
                )
            }

            SymbolType.NUMBER, SymbolType.DOT, SymbolType.PARENTHESIS_CLOSED -> {
                if (countOpenParenthesis > countClosedParenthesis) {
                    isLastSymbolOpenParenthesis = false
                    addElement(
                        equation = equation,
                        element = CLOSED_PARENTHESIS_SYMBOL,
                        symbolType = SymbolType.PARENTHESIS_CLOSED,
                    )
                } else {
                    isLastSymbolOpenParenthesis = true
                    addElement(
                        equation = equation,
                        element = OPEN_PARENTHESIS_SYMBOL,
                        symbolType = SymbolType.PARENTHESIS_OPEN,
                        isNeedMultiply = true
                    )
                }
            }
        }
        lastSymbolType = if (isLastSymbolOpenParenthesis) {
            SymbolType.PARENTHESIS_OPEN
        } else {
            SymbolType.PARENTHESIS_CLOSED
        }

        results.add(calculate(newEquation))
        return newEquation
    }

    private fun showResult() {
        if (_calculatorStateFlow.value.result.isEmpty()) return
        var result = EMPTY_STRING

        val res = _calculatorStateFlow.value.result.toBigDecimalOrNull()?.toDouble()
        if (res != null) {
            result = processResult(res, isNeedRound = true)
        }

        clearData()

        // на основе строки-результата формируем массивы numbers, operations, results, чтобы
        // с результатом можно было работать как с введенным значением
        val equation = addResultList(result)
        changeState(equation, EMPTY_STRING)
    }

    private fun processResult(result: Double, isNeedRound: Boolean = false): String {
        val longres = result.toLong()
        return if (longres.toDouble() == result) { // отбрасываем нулевую дробную часть
            longres.toString()
        } else {
            if (isNeedRound) String.format("%.2f", result) else result.toString()
        }
    }

    private fun addResultList(equation: String): String {
        var newEquation = EMPTY_STRING
        val resArray = equation.toCharArray().filter { it != ' ' }
        resArray.forEach { symbol ->
            newEquation = if (symbol.toString() == OPERATOR_SUBTRACT_SYMBOL)
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

    // добавить оператор, скобку или первую цифру числа
    private fun addElement(
        equation: String,
        element: String,
        symbolType: SymbolType,
        isNeedMultiply: Boolean = false
    ): String {
        // Еcли перед элементом нужно вставить умножение
        var newEquation = if (isNeedMultiply) {
            processOperationInput(
                equation = equation,
                operation = CalculatorOperation.Multiply
            )
        } else equation

        when (symbolType) {
            SymbolType.NUMBER -> numbers.add(element)
            SymbolType.OPERATION -> operations.add(element)
            SymbolType.PARENTHESIS_OPEN -> countOpenParenthesis++
            SymbolType.PARENTHESIS_CLOSED -> countClosedParenthesis++
            else -> {}
        }

        return newEquation + DELIMITER + element
    }

    // добавить к числу разряд или точку
    private fun addDigit(equation: String, symbol: String, symbolType: SymbolType): String {
        if (symbolType != SymbolType.NUMBER || numbers.isEmpty()) return equation

        numbers[numbers.lastIndex] = numbers.last() + symbol
        return equation + symbol
    }

    // заменить оператор или цифру (не разряд числа)
    private fun replaceLastElement(
        equation: String,
        element: String,
        symbolType: SymbolType
    ): String {
        // в текущей реализации подменются только числа и операторы, поэтому другие типы не
        // обрабатываются
        if (symbolType != SymbolType.NUMBER && symbolType != SymbolType.OPERATION) return equation

        // Размер results равен длине выражения (без разделителей). После сalculate всегда
        // делаем results.add(), поэтому, если длина выражения не меняется (когда делаем замену),
        // нужно вручную удалить последний result.
        results.removeLast()

        when (symbolType) {
            SymbolType.NUMBER -> {
                if (numbers.isNotEmpty()) numbers[numbers.lastIndex] = element
            }

            SymbolType.OPERATION -> {
                if (operations.isNotEmpty()) operations[operations.lastIndex] = element
            }

            else -> {}
        }

        return equation.dropLast(1) + element
    }

    private fun calculate(equation: String): String {
        val correctedEquation = correctEquation(equation)
        var result = ZERO_SYMBOL

        try {
            val expr: Expression = ExpressionBuilder(correctedEquation)
                .build()
            val res = expr.evaluate()
            result = processResult(res)
        } catch (e: IllegalArgumentException) {
            e.message?.let { Log.e(HARM_APP_TAG, it) }
            result = EMPTY_STRING
        }

        return result
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
            val symbolType = getLastSymbolType(lastSymbol)

            completedEquation = when (symbolType) {
                SymbolType.OPERATION -> {
                    isEquationCorrect = false
                    completedEquation.dropLast(2) // оператор и разделитель
                }

                SymbolType.PARENTHESIS_OPEN -> {
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
            completedEquation += DELIMITER + CLOSED_PARENTHESIS_SYMBOL
            localCountClosedParenthesis++
        }

        //Заменяем x на *
        completedEquation =
            completedEquation
                .replace(OPERATOR_MULTIPLY_SYMBOL, OPERATOR_MULTIPLY_SYMBOL_MATH)
        //Заменяем ÷ на /
        completedEquation =
            completedEquation
                .replace(OPERATOR_DIVIDE_SYMBOL, OPERATOR_DIVIDE_SYMBOL_MATH)

        return completedEquation
    }

    private fun getLastSymbolType(symbol: String): SymbolType {
        return when (symbol) {
            ZERO_SYMBOL, ONE_SYMBOL, TWO_SYMBOL, THREE_SYMBOL, FOUR_SYMBOL,
            FIVE_SYMBOL, SIX_SYMBOL, SEVEN_SYMBOL, EIGHT_SYMBOL,
            NINE_SYMBOL -> SymbolType.NUMBER

            DOT_SYMBOL -> SymbolType.DOT

            OPERATOR_ADD_SYMBOL, OPERATOR_SUBTRACT_SYMBOL, OPERATOR_MULTIPLY_SYMBOL,
            OPERATOR_DIVIDE_SYMBOL -> SymbolType.OPERATION

            OPEN_PARENTHESIS_SYMBOL -> SymbolType.PARENTHESIS_OPEN
            CLOSED_PARENTHESIS_SYMBOL -> SymbolType.PARENTHESIS_CLOSED

            else -> SymbolType.NOTHING
        }
    }

    private enum class SymbolType {
        NUMBER,
        OPERATION,
        DOT,
        PARENTHESIS_OPEN,
        PARENTHESIS_CLOSED,
        NOTHING
    }

    private companion object {
        const val HARM_APP_TAG = "HarmAppTag"
        const val EMPTY_STRING = ""
        const val DELIMITER = " "
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
        const val OPERATOR_ADD_SYMBOL = "+"
        const val OPERATOR_SUBTRACT_SYMBOL = "-"
        const val OPERATOR_MULTIPLY_SYMBOL = "x"
        const val OPERATOR_DIVIDE_SYMBOL = "÷"
        const val OPERATOR_MULTIPLY_SYMBOL_MATH = "*"
        const val OPERATOR_DIVIDE_SYMBOL_MATH = "/"
    }
}
