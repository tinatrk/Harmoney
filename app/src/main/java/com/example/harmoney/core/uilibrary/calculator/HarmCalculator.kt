package com.example.harmoney.core.uilibrary.calculator

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.harmoney.annotation.UiLibrary
import com.example.harmoney.presentation.calculator.models.CalculatorAction
import com.example.harmoney.presentation.calculator.models.CalculatorOperation
import com.example.harmoney.presentation.calculator.models.CalculatorState
import com.example.harmoney.presentation.calculator.models.CalculatorSymbol
import com.example.harmoney.ui.theme.HarmTheme

/** HarmCalculator
 * - state - CalculatorState
 * - onAction - processing of all clicks
 * - modifier - Modifier
 * - onCalculateClick - additional functionality is expected here when you click on "="
 * (for example, close the area with the calculator)
 * */
@UiLibrary
@Composable
fun HarmCalculator(
    state: CalculatorState,
    onAction: (CalculatorAction) -> Unit,
    modifier: Modifier = Modifier,
    onCalculateClick: (() -> Unit) = {}
) {
    val colors = HarmTheme.colors

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .wrapContentHeight()
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = state.equation.ifEmpty { state.result },
            style = HarmTheme.typography.titleLarge,
            color = colors.onSurface,
            textAlign = TextAlign.End
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = if (state.equation.isNotEmpty()) state.result else "",
            style = HarmTheme.typography.titleMedium,
            color = colors.onSurfaceContainerLow,
            textAlign = TextAlign.End
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.weight(
                    Const.MANAGEMENT_WIDTH_IN_BUTTONS / Const.CALCULATOR_WIDTH_IN_BUTTONS
                ), // 3/4
            ) {
                CalculatorManagementBlock(onAction = onAction)
                CalculationDigitsBlock(onAction = onAction)
            }

            CalculatingMathBlock(
                modifier = Modifier.weight(
                    Const.OPERATION_WIDTH_IN_BUTTONS / Const.CALCULATOR_WIDTH_IN_BUTTONS
                ), // 1/4
                onAction = onAction,
                onCalculateClick
            )
        }
    }
}

@Composable
private fun CalculatorManagementBlock(
    modifier: Modifier = Modifier,
    onAction: (CalculatorAction) -> Unit
) {
    Row(modifier = modifier) {
        CalculatorButton(
            modifier = Modifier.weight(
                Const.COMMON_BUTTON_WIDTH / Const.MANAGEMENT_WIDTH_IN_BUTTONS
            ), // 1/3
            symbol = CalculatorSymbol.CLEAR.symbol,
            type = CalculatorButtonType.MANAGEMENT
        ) { onAction(CalculatorAction.Clear) }

        CalculatorButton(
            modifier = Modifier.weight(
                Const.COMMON_BUTTON_WIDTH / Const.MANAGEMENT_WIDTH_IN_BUTTONS
            ), // 1/3
            symbol = CalculatorSymbol.DELETE_LAST.symbol,
            type = CalculatorButtonType.MANAGEMENT
        ) { onAction(CalculatorAction.Delete) }

        CalculatorButton(
            modifier = Modifier.weight(
                Const.COMMON_BUTTON_WIDTH / Const.MANAGEMENT_WIDTH_IN_BUTTONS
            ), // 1/3
            symbol = CalculatorSymbol.PARENTHESIS_OPEN.symbol + " "
                    + CalculatorSymbol.PARENTHESIS_CLOSED.symbol,
            type = CalculatorButtonType.MANAGEMENT
        ) { onAction(CalculatorAction.Parenthesis) }
    }
}

@Composable
private fun CalculationDigitsBlock(
    modifier: Modifier = Modifier,
    onAction: (CalculatorAction) -> Unit
) {
    val symbolsWithoutLostRow = CalculatorSymbol.getNumbers()
        .filter { it.symbol != CalculatorSymbol.ZERO.symbol }
        .map { it.symbol }

    Column(modifier = modifier) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(Const.NUMBERS_WIDTH_IN_BUTTONS.toInt()),
            userScrollEnabled = false,
        ) {
            items(symbolsWithoutLostRow) { number ->
                CalculatorButton(
                    symbol = number,
                    type = CalculatorButtonType.NUMBER
                ) { onAction(CalculatorAction.Number(number)) }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            CalculatorButton(
                modifier = modifier.weight(
                    Const.DOUBLE_BUTTON_WIDTH / Const.NUMBERS_WIDTH_IN_BUTTONS
                ), // 2/3
                symbol = CalculatorSymbol.ZERO.symbol,
                type = CalculatorButtonType.NUMBER,
            ) { onAction(CalculatorAction.Number(CalculatorSymbol.ZERO.symbol)) }
            CalculatorButton(
                modifier = modifier.weight(
                    Const.COMMON_BUTTON_WIDTH / Const.NUMBERS_WIDTH_IN_BUTTONS
                ), // 1/3
                symbol = CalculatorSymbol.DOT.symbol,
                type = CalculatorButtonType.NUMBER,
            ) { onAction(CalculatorAction.Decimal) }
        }
    }
}

@Composable
private fun CalculatingMathBlock(
    modifier: Modifier,
    onAction: (CalculatorAction) -> Unit,
    onCalculateClick: () -> Unit,
) {
    val operations = listOf(
        CalculatorOperation.Divide,
        CalculatorOperation.Multiply,
        CalculatorOperation.Subtract,
        CalculatorOperation.Add
    )
    Column(
        modifier = modifier
    ) {
        operations.forEach { operation ->
            CalculatorButton(
                modifier = Modifier.fillMaxWidth(),
                symbol = operation.symbol,
                type = CalculatorButtonType.OPERATION
            ) { onAction(CalculatorAction.Operation(operation)) }
        }

        CalculatorButton(
            modifier = Modifier.fillMaxWidth(),
            symbol = CalculatorSymbol.EQUALITY.symbol,
            type = CalculatorButtonType.EQUALITY
        ) {
            onAction(CalculatorAction.Calculate)
            onCalculateClick()
        }
    }
}

@Composable
private fun CalculatorButton(
    modifier: Modifier = Modifier,
    symbol: String,
    type: CalculatorButtonType,
    onClick: () -> Unit,
) {
    val colors = CalculatorButtonColors(type)
    val borderColor = if (type == CalculatorButtonType.EQUALITY) {
        colors.first
    } else {
        HarmTheme.colors.outline
    }
    Button(
        modifier = modifier.padding(horizontal = 4.dp),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = colors.first,
            contentColor = colors.second
        ),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Text(
            text = symbol,
            style = HarmTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
    }
}

/** Pair<BackgroundColor, TintColor> */
@Composable
private fun CalculatorButtonColors(type: CalculatorButtonType): Pair<Color, Color> {
    val colors = HarmTheme.colors
    return when (type) {
        CalculatorButtonType.NUMBER -> {
            colors.surfaceContainer to colors.onSurfaceContainer
        }

        CalculatorButtonType.OPERATION -> {
            colors.secondaryContainer to colors.onSecondaryContainer
        }

        CalculatorButtonType.MANAGEMENT -> {
            colors.surfaceContainerHighest to colors.onSurfaceContainer
        }

        CalculatorButtonType.EQUALITY -> colors.primary to colors.onPrimary
    }
}

private enum class CalculatorButtonType {
    NUMBER,
    OPERATION,
    MANAGEMENT,
    EQUALITY,
}

private object Const {
    const val CALCULATOR_WIDTH_IN_BUTTONS = 4f
    const val MANAGEMENT_WIDTH_IN_BUTTONS = 3f
    const val OPERATION_WIDTH_IN_BUTTONS = 1f
    const val NUMBERS_WIDTH_IN_BUTTONS = 3f
    const val DOUBLE_BUTTON_WIDTH = 2f
    const val COMMON_BUTTON_WIDTH = 1f
}
