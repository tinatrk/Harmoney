package com.example.harmoney.core.uilibrary.graphics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.harmoney.presentation.models.PieChartItem
import com.example.harmoney.ui.theme.HarmTheme

object HarmGraphic {
    @Composable
    fun PieChart(
        items: List<PieChartItem>,
        total: Float,
        modifier: Modifier = Modifier,
    ) {
        val strokeWidthPx = 52f
        val screenDensity = LocalDensity.current.density
        // Независимо от поставленных ограничений, canvas будет рисовать на strokeWidthPx/2 выше
        // заявленных размеров области. Добавляем отступ (в dp), чтобы всегда получать график
        // в ожидаемом месте.
        val additionalPadding = remember {
            mutableStateOf(
                (strokeWidthPx / screenDensity) / 2
            )
        }

        val borderColor = HarmTheme.colors.outline
        val isNeedBorder = !HarmTheme.colors.isDark

        Canvas(
            modifier = modifier
                .padding(additionalPadding.value.dp)
                .fillMaxWidth(0.50f)
                .aspectRatio(1f)
        ) {
            if (total <= 0f) return@Canvas

            items.forEach { item ->
                if (isNeedBorder){
                    drawArc(
                        color = borderColor,
                        startAngle = item.startAngle,
                        sweepAngle = item.sweepAngle,
                        useCenter = false,
                        style = Stroke(width = strokeWidthPx + 4f)
                    )
                }

                drawArc(
                    color = Color(item.colorValue),
                    startAngle = item.startAngle,
                    sweepAngle = item.sweepAngle,
                    useCenter = false,
                    style = Stroke(width = strokeWidthPx)
                )
            }
        }
    }
}
