package com.example.harmoney.core.uilibrary.graphics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.harmoney.annotation.UiLibrary
import com.example.harmoney.presentation.models.PieChartItem

@UiLibrary
object HarmGraphic {
    private const val PIE_STROKE_WIDTH_PX = 52f

    @Composable
    fun PieChart(
        items: List<PieChartItem>,
        modifier: Modifier = Modifier,
    ) {
        val screenDensity = LocalDensity.current.density
        // Независимо от поставленных ограничений, canvas будет рисовать на PIE_STROKE_WIDTH_PX/2
        // за пределами заявленных размеров области (по всем сторонам).
        // Добавляем отступ (в dp), чтобы всегда получать график в ожидаемом месте.
        val additionalPadding = remember {
            mutableFloatStateOf(
                (PIE_STROKE_WIDTH_PX / screenDensity) / 2
            )
        }

        Canvas(
            modifier = modifier
                .padding(additionalPadding.floatValue.dp)
                .fillMaxWidth(1f/2)
                .aspectRatio(1f)
        ) {

            items.forEach { item ->
                drawArc(
                    color = Color(item.colorValue),
                    startAngle = item.startAngle,
                    sweepAngle = item.sweepAngle,
                    useCenter = false,
                    style = Stroke(width = PIE_STROKE_WIDTH_PX)
                )
            }
        }
    }
}
