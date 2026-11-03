package com.example.harmoney.ui.screens.categoryStatisticsScreen

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.harmoney.R
import com.example.harmoney.core.uilibrary.dialogs.HarmDialog
import com.example.harmoney.presentation.categoryStatistics.models.CategoryStatisticsEvent
import com.example.harmoney.presentation.categoryStatistics.models.FirstDayMonthError
import com.example.harmoney.ui.theme.HarmTheme

@Composable
fun CategoryStatisticsDialog(
    firstDayMonthText: String,
    firstDayMonthError: FirstDayMonthError,
    isOpenedFirstDayMonthDialog: Boolean,
    onEvent: (CategoryStatisticsEvent) -> Unit,
) {
    val supportText =
        when (firstDayMonthError) {
            is FirstDayMonthError.None -> ""
            is FirstDayMonthError.OutOfRange -> {
                stringResource(
                    R.string.error_incorrect_first_day_month_pattern,
                    firstDayMonthError.minDay,
                    firstDayMonthError.maxDay
                )
            }
        }

    if (isOpenedFirstDayMonthDialog) {
        HarmDialog.HarmSetFirstDayMonthDialog(
            numberString = firstDayMonthText,
            onNumberChanged = { newText ->
                onEvent(
                    CategoryStatisticsEvent.OnFirstDayMonthTextChanged(
                        newText
                    )
                )
            },
            onConfirmation = { onEvent(CategoryStatisticsEvent.OnFirstDayMonthDialogConfirm) },
            onDismissRequest = {
                onEvent(CategoryStatisticsEvent.OnFirstDayMonthDialogDismiss)
            },
            onTextFieldDoneAction = {
                onEvent(CategoryStatisticsEvent.OnFirstDayMonthDialogConfirm)
            },
            isError = (firstDayMonthError != FirstDayMonthError.None),
            supportingText = supportText,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun CategoryStatisticsDialog_DarkPreview() {
    HarmTheme(darkTheme = true) {
        CategoryStatisticsDialog(
            onEvent = {},
            firstDayMonthError = FirstDayMonthError.None,
            firstDayMonthText = "1",
            isOpenedFirstDayMonthDialog = true
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun CategoryStatisticsDialog_LightPreview() {
    HarmTheme(darkTheme = false) {
        CategoryStatisticsDialog(
            onEvent = {},
            firstDayMonthError = FirstDayMonthError.None,
            firstDayMonthText = "1",
            isOpenedFirstDayMonthDialog = true
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun CategoryStatisticsDialog_Error_DarkPreview() {
    HarmTheme(darkTheme = true) {
        CategoryStatisticsDialog(
            onEvent = {},
            firstDayMonthError = FirstDayMonthError.OutOfRange(minDay = 1, maxDay = 28),
            firstDayMonthText = "90",
            isOpenedFirstDayMonthDialog = true
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun CategoryStatisticsDialog_Error_LightPreview() {
    HarmTheme(darkTheme = false) {
        CategoryStatisticsDialog(
            onEvent = {},
            firstDayMonthError = FirstDayMonthError.OutOfRange(minDay = 1, maxDay = 28),
            firstDayMonthText = "90",
            isOpenedFirstDayMonthDialog = true
        )
    }
}
