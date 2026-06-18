package com.example.harmoney.core.uilibrary.graphics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.harmoney.annotation.UiLibrary
import com.example.harmoney.domain.models.CategoryColors
import com.example.harmoney.presentation.models.PieChartItem
import com.example.harmoney.ui.theme.HarmTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@UiLibrary
object HarmGraphic {
    private const val PIE_STROKE_WIDTH_PX_SMALL = 32f
    private const val PIE_STROKE_WIDTH_PX_NORMAL = 52f
    private const val PIE_STROKE_WIDTH_PX_BIG = 72f

    private const val SMALL_SCREEN_DP = 320
    private const val NORMAL_SCREEN_DP = 480

    @Composable
    fun PieChart(
        items: ImmutableList<PieChartItem>,
        pieStrokeWidthPx: Float,
        modifier: Modifier = Modifier,
    ) {
        val screenDensity = LocalDensity.current.density
        // Независимо от поставленных ограничений, canvas будет рисовать на PIE_STROKE_WIDTH_PX/2
        // за пределами заявленных размеров области (по всем сторонам).
        // Добавляем отступ (в dp), чтобы всегда получать график в ожидаемом месте.
        val halfPieStrokeWidthDp = remember {
            mutableFloatStateOf((pieStrokeWidthPx / screenDensity) / 2)
        }

        Canvas(
            modifier = modifier
                .padding(halfPieStrokeWidthDp.floatValue.dp)
                .fillMaxWidth()
                .aspectRatio(1f)
        ) {

            items.forEach { item ->
                drawArc(
                    color = Color(item.colorValue),
                    startAngle = item.startAngle,
                    sweepAngle = item.sweepAngle,
                    useCenter = false,
                    style = Stroke(width = pieStrokeWidthPx)
                )
            }
        }
    }

    @Composable
    fun PieChartWithTitle(
        items: ImmutableList<PieChartItem>,
        title: String,
        modifier: Modifier = Modifier,
    ) {
        val configuration = LocalConfiguration.current
        val screenSize = configuration.screenWidthDp
        val pieStrokeWidthPx = when {
            screenSize <= SMALL_SCREEN_DP -> PIE_STROKE_WIDTH_PX_SMALL
            screenSize <= NORMAL_SCREEN_DP -> PIE_STROKE_WIDTH_PX_NORMAL
            else -> PIE_STROKE_WIDTH_PX_BIG
        }

        val screenDensity = LocalDensity.current.density
        // отступ, чтобы title был строго внутри pieChart
        val pieStrokeWidthDp = remember { mutableFloatStateOf((pieStrokeWidthPx / screenDensity)) }

        Box(
            modifier = modifier.fillMaxWidth(1f / 2),
            contentAlignment = Alignment.Center
        ) {
            if (items.isNotEmpty()) {
                PieChart(items = items, pieStrokeWidthPx = pieStrokeWidthPx)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(pieStrokeWidthDp.floatValue.dp + 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        style = HarmTheme.typography.bodyLarge,
                        color = HarmTheme.colors.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun PieChart_DarkPreview() {
    HarmTheme(darkTheme = true) {
        HarmGraphic.PieChart(
            pieStrokeWidthPx = 52f,
            items = persistentListOf(
                PieChartItem(
                    value = "15 000 ₽",
                    colorValue = CategoryColors.BLUE_T80.background,
                    startAngle = -90f,
                    sweepAngle = 205.69f,
                ),
                PieChartItem(
                    value = "7 500 ₽",
                    colorValue = CategoryColors.ORANGE_T70.background,
                    startAngle = 117.69f,
                    sweepAngle = 101.84f,
                ),
                PieChartItem(
                    value = "3 500 ₽",
                    colorValue = CategoryColors.VIOLET_T68.background,
                    startAngle = 221.53f,
                    sweepAngle = 46.46f,
                )
            )
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun PieChart_LightPreview() {
    HarmTheme(darkTheme = false) {
        HarmGraphic.PieChart(
            pieStrokeWidthPx = 52f,
            items = persistentListOf(
                PieChartItem(
                    value = "15 000 ₽",
                    colorValue = CategoryColors.BLUE_T80.background,
                    startAngle = -90f,
                    sweepAngle = 205.69f,
                ),
                PieChartItem(
                    value = "7 500 ₽",
                    colorValue = CategoryColors.ORANGE_T70.background,
                    startAngle = 117.69f,
                    sweepAngle = 101.84f,
                ),
                PieChartItem(
                    value = "3 500 ₽",
                    colorValue = CategoryColors.VIOLET_T68.background,
                    startAngle = 221.53f,
                    sweepAngle = 46.46f,
                )
            )
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun PieChartWithTitle_DarkPreview() {
    HarmTheme(darkTheme = true) {
        Box(modifier = Modifier.fillMaxWidth()) {
            HarmGraphic.PieChartWithTitle(
                title = "26 000 ₽",
                items = persistentListOf(
                    PieChartItem(
                        value = "15 000 ₽",
                        colorValue = CategoryColors.BLUE_T80.background,
                        startAngle = -90f,
                        sweepAngle = 205.69f,
                    ),
                    PieChartItem(
                        value = "7 500 ₽",
                        colorValue = CategoryColors.ORANGE_T70.background,
                        startAngle = 117.69f,
                        sweepAngle = 101.84f,
                    ),
                    PieChartItem(
                        value = "3 500 ₽",
                        colorValue = CategoryColors.VIOLET_T68.background,
                        startAngle = 221.53f,
                        sweepAngle = 46.46f,
                    )
                )
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun PieChartWithTitle_LightPreview() {
    HarmTheme(darkTheme = false) {
        Box(modifier = Modifier.fillMaxWidth()) {
            HarmGraphic.PieChartWithTitle(
                title = "26 000 ₽",
                items = persistentListOf(
                    PieChartItem(
                        value = "15 000 ₽",
                        colorValue = CategoryColors.BLUE_T80.background,
                        startAngle = -90f,
                        sweepAngle = 205.69f,
                    ),
                    PieChartItem(
                        value = "7 500 ₽",
                        colorValue = CategoryColors.ORANGE_T70.background,
                        startAngle = 117.69f,
                        sweepAngle = 101.84f,
                    ),
                    PieChartItem(
                        value = "3 500 ₽",
                        colorValue = CategoryColors.VIOLET_T68.background,
                        startAngle = 221.53f,
                        sweepAngle = 46.46f,
                    )
                )
            )
        }
    }
}
