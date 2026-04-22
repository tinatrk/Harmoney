package com.example.harmoney.presentation.calculator.viewModel

import androidx.lifecycle.ViewModel
import com.example.harmoney.presentation.calculator.models.CalculatorAction
import com.example.harmoney.presentation.calculator.models.CalculatorOperation
import com.example.harmoney.presentation.calculator.models.CalculatorState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CalculatorViewModel(
    private val calculatorHandler: CalculatorHandler
) : ViewModel() {
    private val _calculatorStateFlow = MutableStateFlow(CalculatorState())
    val calculatorStateFlow = _calculatorStateFlow.asStateFlow()

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
            is CalculatorAction.CloseCalculator -> calculatorHandler.clearData()
        }
    }

    private fun changeState(equation: String, result: String) {
        _calculatorStateFlow.value = _calculatorStateFlow.value.copy(
            equation = equation,
            result = result
        )
    }

    private fun initializeEquation(initEquation: String) {
        val equation = calculatorHandler.initCalculator(initEquation)
        val result = calculatorHandler.getResult()
        changeState(equation = equation, result = result)
    }

    private fun enterNumber(symbol: String) {
        val equation = calculatorHandler.processNumberInput(
            equation = _calculatorStateFlow.value.equation,
            symbol = symbol
        )

        val result = calculatorHandler.getResult()
        changeState(equation, result)
    }

    private fun clear() {
        calculatorHandler.clearData()
        changeState(equation = EMPTY_STRING, result = EMPTY_STRING)
    }

    private fun delete() {
        val equation =
            calculatorHandler.processDeleting(equation = _calculatorStateFlow.value.equation)

        val result = calculatorHandler.getResult()
        changeState(equation, result)
    }

    private fun enterOperation(operation: CalculatorOperation) {
        val equation = calculatorHandler.processOperationInput(
            equation = _calculatorStateFlow.value.equation,
            operation = operation
        )
        val result = calculatorHandler.getResult()
        changeState(equation, result)
    }

    private fun enterDecimal() {
        val equation =
            calculatorHandler.processDecimalInput(equation = _calculatorStateFlow.value.equation)
        val result = calculatorHandler.getResult()
        changeState(equation, result)
    }

    private fun enterParenthesis() {
        val equation = calculatorHandler
            .processParenthesisInput(equation = _calculatorStateFlow.value.equation)

        val result = calculatorHandler.getResult()
        changeState(equation, result)
    }

    private fun showResult() {
        if (_calculatorStateFlow.value.result.isEmpty()) return

        val result = calculatorHandler.getResult(isNeedProcessing = true)

        // Задаем начальное значение калькулятору, чтобы с результатом можно было работать
        // как с введенным значением
        val equation = calculatorHandler.initCalculator(result)
        changeState(equation, EMPTY_STRING)
    }

    private companion object {
        const val EMPTY_STRING = ""
    }
}
