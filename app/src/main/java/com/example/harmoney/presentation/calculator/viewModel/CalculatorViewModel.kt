package com.example.harmoney.presentation.calculator.viewModel

import com.example.harmoney.base.BaseViewModel
import com.example.harmoney.presentation.calculator.models.CalculatorAction
import com.example.harmoney.presentation.calculator.models.CalculatorEvent
import com.example.harmoney.presentation.calculator.models.CalculatorOperation
import com.example.harmoney.presentation.calculator.models.CalculatorResult
import com.example.harmoney.presentation.calculator.models.CalculatorState
import kotlinx.coroutines.flow.update

class CalculatorViewModel(
    private val calculatorHandler: CalculatorHandler
) : BaseViewModel<CalculatorEvent, CalculatorAction, CalculatorState>(CalculatorState()) {

    override val tag: String = CalculatorViewModel::class.java.simpleName ?: ""

    override fun obtainEvent(event: CalculatorEvent) {
        when (event) {
            is CalculatorEvent.OnOpenCalculator -> initializeEquation(event.initEquation)
            is CalculatorEvent.OnEnterNumber -> onEnterNumber(event.number)
            is CalculatorEvent.OnClearClick -> clear()
            is CalculatorEvent.OnDeleteClick -> onDelete()
            is CalculatorEvent.OnEnterOperation -> onEnterOperation(event.operation)
            is CalculatorEvent.OnEnterDecimalDot -> onEnterDecimal()
            is CalculatorEvent.OnCalculateClick -> showResult()
            is CalculatorEvent.OnEnterParenthesis -> onEnterParenthesis()
            is CalculatorEvent.OnCloseCalculator -> onCloseCalculator()
        }
    }

    private fun changeState(
        equation: String,
        result: CalculatorResult,
    ) {
        writableState.update {
            it.copy(
                equation = equation,
                resultString = result.resultString,
                result = result.resultNumeric,
            )
        }
    }

    private fun initializeEquation(initEquation: String) {
        val equation = calculatorHandler.initCalculator(initEquation)
        val result = calculatorHandler.getResult()
        changeState(equation = equation, result = result)
    }

    private fun onEnterNumber(symbol: String) {
        val equation = calculatorHandler.processNumberInput(
            equation = state.value.equation,
            symbol = symbol
        )

        val result = calculatorHandler.getResult()
        changeState(equation = equation, result = result)
    }

    private fun clear() {
        calculatorHandler.clearData()
        changeState(
            equation = EMPTY_STRING,
            result = CalculatorResult(resultNumeric = ZERO_RESULT, resultString = EMPTY_STRING)
        )
    }

    private fun onDelete() {
        val equation = calculatorHandler.processDeleting(equation = state.value.equation)

        val result = calculatorHandler.getResult()
        changeState(equation = equation, result = result)
    }

    private fun onEnterOperation(operation: CalculatorOperation) {
        val equation = calculatorHandler.processOperationInput(
            equation = state.value.equation,
            operation = operation
        )
        val result = calculatorHandler.getResult()
        changeState(equation = equation, result = result)
    }

    private fun onEnterDecimal() {
        val equation = calculatorHandler.processDecimalInput(equation = state.value.equation)
        val result = calculatorHandler.getResult()
        changeState(equation = equation, result = result)
    }

    private fun onEnterParenthesis() {
        val equation = calculatorHandler.processParenthesisInput(equation = state.value.equation)

        val result = calculatorHandler.getResult()
        changeState(equation = equation, result = result)
    }

    private fun showResult() {
        if (state.value.resultString.isEmpty()) return

        val result = calculatorHandler.getResult(isNeedProcessing = true)

        // Задаем начальное значение калькулятору, чтобы с результатом можно было работать
        // как с введенным значением
        val equation = calculatorHandler.initCalculator(result.resultString)
        changeState(
            equation = equation,
            result = CalculatorResult(
                resultNumeric = result.resultNumeric, resultString = EMPTY_STRING
            )
        )
    }

    private fun onCloseCalculator() {
        calculatorHandler.clearData()
    }

    private companion object {
        const val EMPTY_STRING = ""
        const val ZERO_RESULT = 0.0
    }
}
