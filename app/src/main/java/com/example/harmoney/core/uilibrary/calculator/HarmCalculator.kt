package com.example.harmoney.core.uilibrary.calculator

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
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.harmoney.presentation.calculator.models.CalculatorAction
import com.example.harmoney.presentation.calculator.models.CalculatorOperation
import com.example.harmoney.presentation.calculator.models.CalculatorState
import com.example.harmoney.ui.theme.HarmTheme

/** HarmCalculator
 * - state - CalculatorState
 * - onAction - processing of all clicks
 * - modifier - Modifier
 * - onCalculateClick - additional functionality is expected here when you click on "="
 * (for example, close the area with the calculator)
 * */
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
                modifier = Modifier.weight(3f), // 3/4
            ) {
                CalculatorManagementBlock(onAction = onAction)
                CalculationDigitsBlock(onAction = onAction)
            }

            CalculatingMathBlock(
                modifier = Modifier.weight(1f), // 1/4
                onAction = onAction,
                onCalculateClick
            )
        }
    }
}

@Composable
fun CalculatorManagementBlock(
    modifier: Modifier = Modifier,
    onAction: (CalculatorAction) -> Unit
) {
    Row(modifier = modifier) {
        CalculatorButton(
            modifier = Modifier.weight(1f), // 1/3
            symbol = "C",
            type = CalculatorButtonType.MANAGEMENT
        ) { onAction(CalculatorAction.Clear) }

        CalculatorButton(
            modifier = Modifier.weight(1f), // 1/3
            symbol = "Del",
            type = CalculatorButtonType.MANAGEMENT
        ) { onAction(CalculatorAction.Delete) }

        CalculatorButton(
            modifier = Modifier.weight(1f), // 1/3
            symbol = "( )",
            type = CalculatorButtonType.MANAGEMENT
        ) { onAction(CalculatorAction.Parenthesis) }
    }
}

@Composable
fun CalculationDigitsBlock(
    modifier: Modifier = Modifier,
    onAction: (CalculatorAction) -> Unit
) {
    val numbers = listOf(
        "7", "8", "9",
        "4", "5", "6",
        "1", "2", "3",
    )

    Column(modifier = modifier) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            userScrollEnabled = false,
        ) {
            items(numbers) { number ->
                CalculatorButton(
                    symbol = number,
                    type = CalculatorButtonType.DIGIT
                ) { onAction(CalculatorAction.Number(number)) }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            CalculatorButton(
                modifier = modifier.weight(2f), // 2/3
                symbol = "0",
                type = CalculatorButtonType.DIGIT
            ) { onAction(CalculatorAction.Number("0")) }
            CalculatorButton(
                modifier = modifier.weight(1f), // 1/3
                symbol = ".",
                type = CalculatorButtonType.DIGIT
            ) { onAction(CalculatorAction.Decimal) }
        }
    }
}

@Composable
fun CalculatingMathBlock(
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
                type = CalculatorButtonType.MATH_OPERATION
            ) { onAction(CalculatorAction.Operation(operation)) }
        }

        CalculatorButton(
            modifier = Modifier.fillMaxWidth(),
            symbol = "=",
            type = CalculatorButtonType.RESULT
        ) {
            onAction(CalculatorAction.Calculate)
            onCalculateClick()
        }
    }
}

@Composable
fun CalculatorButton(
    modifier: Modifier = Modifier,
    symbol: String,
    type: CalculatorButtonType,
    onClick: () -> Unit,
) {
    val colors = CalculatorButtonColors(type)
    IconButton(
        modifier = modifier.padding(horizontal = 4.dp),
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = colors.first,
            contentColor = colors.second
        )
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
fun CalculatorButtonColors(type: CalculatorButtonType): Pair<Color, Color> {
    val colors = HarmTheme.colors
    return when (type) {
        CalculatorButtonType.DIGIT -> colors.surface to colors.onSurface

        CalculatorButtonType.MATH_OPERATION -> colors.primary to colors.onPrimary

        CalculatorButtonType.MANAGEMENT -> colors.outline to colors.surface

        CalculatorButtonType.RESULT -> colors.secondary to colors.onSecondary
    }
}

enum class CalculatorButtonType {
    DIGIT,
    MATH_OPERATION,
    MANAGEMENT,
    RESULT
}
