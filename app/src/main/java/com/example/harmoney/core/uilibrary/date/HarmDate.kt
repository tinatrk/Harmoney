package com.example.harmoney.core.uilibrary.date

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.harmoney.R
import com.example.harmoney.annotation.UiLibrary
import com.example.harmoney.core.uilibrary.buttons.HarmButton
import com.example.harmoney.domain.models.StatisticsPeriod
import com.example.harmoney.ui.mappers.StatisticsPeriodUiMapper.toStringRes
import com.example.harmoney.ui.theme.HarmTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

/**
 * - `HarmStatisticPeriodList` - Text list of periods for statistic logic
 * - `HarmDatePickerModal` - Date Picker Dialog
 */
@UiLibrary
object HarmDate {
    /** Text list of periods for statistic logic */
    @Composable
    fun HarmStatisticPeriodList(
        data: String,
        periods: ImmutableList<StatisticsPeriod>,
        selectedPeriod: StatisticsPeriod,
        onPeriodClick: (StatisticsPeriod) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                periods.forEach { period ->

                    val borderColor = if (period.id == selectedPeriod.id) {
                        HarmTheme.colors.primary
                    } else {
                        Color.Transparent
                    }
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 6.dp)
                            .border(
                                width = 1.dp, color = borderColor,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 4.dp)

                            .clickable { onPeriodClick(period) },
                        text = stringResource(period.toStringRes()),
                        style = if (period.id == selectedPeriod.id) {
                            HarmTheme.typography.titleSmallSemiBold
                        } else {
                            HarmTheme.typography.titleSmall
                        },
                        color = if (period.id == selectedPeriod.id) {
                            HarmTheme.colors.primary
                        } else {
                            HarmTheme.colors.onSurfaceContainer
                        },
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = data,
                    style = HarmTheme.typography.titleSmall,
                    color = HarmTheme.colors.onSurface
                )
            }
        }
    }

    /** Date Picker Dialog */
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun HarmDatePickerModal(
        onDateSelected: (Long?) -> Unit,
        onDismiss: () -> Unit,
    ) {
        val colors = HarmTheme.colors
        val datePickerState = rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                HarmButton.HarmPrimaryButton(
                    text = stringResource(R.string.btn_dialog_select_text),
                    onClick = {
                        onDateSelected(datePickerState.selectedDateMillis)
                        onDismiss()
                    }
                )
            },
            dismissButton = {
                HarmButton.HarmSecondaryButton(
                    text = stringResource(R.string.btn_dialog_cancel_text),
                    onClick = onDismiss
                )
            },
            colors = DatePickerDefaults.colors(
                containerColor = colors.surfaceContainerHigh,
            ),
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = colors.surfaceContainerHigh,
                    titleContentColor = colors.onSurface,
                    headlineContentColor = colors.onSurface,
                    weekdayContentColor = colors.onSurface,
                    subheadContentColor = colors.onSurfaceVariant,
                    navigationContentColor = colors.onSurfaceVariant,

                    yearContentColor = colors.onSurface,
                    disabledYearContentColor = colors.onSurfaceContainerLow,
                    currentYearContentColor = colors.onInfo,
                    selectedYearContentColor = colors.onPrimary,
                    disabledSelectedYearContentColor = colors.onSurfaceContainerLow,
                    selectedYearContainerColor = colors.primary,
                    disabledSelectedYearContainerColor = colors.surfaceVariant,

                    dayContentColor = colors.onSurface,
                    disabledDayContentColor = colors.onSurfaceContainerLow,
                    selectedDayContentColor = colors.onPrimary,
                    disabledSelectedDayContentColor = colors.onSurfaceContainerLow,
                    selectedDayContainerColor = colors.primary,
                    disabledSelectedDayContainerColor = colors.surfaceVariant,

                    todayContentColor = colors.onInfo,
                    todayDateBorderColor = colors.onInfo,
                    dayInSelectionRangeContainerColor = colors.onInfo,
                    dayInSelectionRangeContentColor = colors.onInfo,
                    dividerColor = colors.outline,

                    dateTextFieldColors = null,
                ),
                showModeToggle = false,
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun HarmStatisticPeriodList_DarkPreview() {
    HarmTheme(darkTheme = true) {
        HarmDate.HarmStatisticPeriodList(
            data = "01.03.2026 - 31.03.2026",
            periods = StatisticsPeriod.entries.toImmutableList(),
            selectedPeriod = StatisticsPeriod.CURRENT_MONTH,
            onPeriodClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun HarmStatisticPeriodList_LightPreview() {
    HarmTheme(darkTheme = false) {
        HarmDate.HarmStatisticPeriodList(
            data = "01.03.2026 - 31.03.2026",
            periods = StatisticsPeriod.entries.toImmutableList(),
            selectedPeriod = StatisticsPeriod.CURRENT_MONTH,
            onPeriodClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun HarmDatePickerModal_DarkPreview() {
    HarmTheme(darkTheme = true) {
        HarmDate.HarmDatePickerModal(
            onDateSelected = {},
            onDismiss = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun HarmDatePickerModal_LightPreview() {
    HarmTheme(darkTheme = false) {
        HarmDate.HarmDatePickerModal(
            onDateSelected = {},
            onDismiss = {}
        )
    }
}
