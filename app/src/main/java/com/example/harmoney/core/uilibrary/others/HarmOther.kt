package com.example.harmoney.core.uilibrary.others

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.harmoney.R
import com.example.harmoney.core.uilibrary.buttons.HarmButton
import com.example.harmoney.presentation.models.StatisticPeriod
import com.example.harmoney.ui.theme.HarmTheme

/**
 * - `HarmStatisticPeriodList` - Text list of periods for statistic logic
 * - `HarmDatePickerModal` - Date Picker Dialog
 */
object HarmOther {
    /** Text list of periods for statistic logic */
    @Composable
    fun HarmStatisticPeriodList(
        data: String,
        periods: List<StatisticPeriod>,
        selectedPeriodId: Long,
        onPeriodClick: (Long) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            periods.forEach { period ->
                Text(
                    modifier = Modifier
                        .padding(horizontal = 6.dp)
                        .clickable { onPeriodClick(period.id) },
                    text = stringResource(period.textRes),
                    style = if (period.id == selectedPeriodId) {
                        HarmTheme.typography.titleSmallSemiBold
                    } else {
                        HarmTheme.typography.titleSmall
                    },
                    color = if (period.id == selectedPeriodId) {
                        HarmTheme.colors.primary
                    } else {
                        HarmTheme.colors.onSurfaceContainer
                    }
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
                containerColor = colors.surface,
            ),
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = colors.surface,
                    titleContentColor = colors.onSurface,
                    headlineContentColor = colors.onSurface,
                    weekdayContentColor = colors.primary,
                    subheadContentColor = colors.onSurface,
                    navigationContentColor = colors.primary,

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
