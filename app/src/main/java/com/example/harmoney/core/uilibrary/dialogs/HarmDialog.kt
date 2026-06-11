package com.example.harmoney.core.uilibrary.dialogs

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.harmoney.R
import com.example.harmoney.annotation.UiLibrary
import com.example.harmoney.core.uilibrary.buttons.HarmButton
import com.example.harmoney.core.uilibrary.textfields.HarmTextField
import com.example.harmoney.ui.theme.HarmTheme

/**
 * - `HarmCommonDialog` - base dialog with title, buttons and content
 * - `HarmConfirmingDialog` - A dialog with a question for user and ok/cancel buttons
 * - `HarmSetFirstDayMonthDialog` - A dialog to set the first day of the month
 * - `HarmWarningDialog' - A dialog with a warning and one ok button
 */
@UiLibrary
object HarmDialog {
    /** Base dialog with title, buttons and content */
    @Composable
    fun HarmCommonDialog(
        onDismissRequest: () -> Unit,
        modifier: Modifier = Modifier,
        dialogTitle: String? = null,
        @DrawableRes iconId: Int? = null,
        iconContentDescription: String? = null,
        dismissButton: @Composable (() -> Unit)?,
        confirmButton: @Composable () -> Unit,
        dialogContent: @Composable() (() -> Unit)? = null,
    ) {
        AlertDialog(
            modifier = modifier,
            icon = iconId?.let {
                {
                    Icon(
                        painter = painterResource(iconId),
                        contentDescription = iconContentDescription,
                        tint = HarmTheme.colors.onSurfaceVariant
                    )
                }
            },
            title = dialogTitle?.let {
                {
                    Text(
                        text = dialogTitle,
                        style = HarmTheme.typography.titleLargeSemiBold,
                        textAlign = TextAlign.Center
                    )
                }
            },
            text = dialogContent,
            onDismissRequest = onDismissRequest,
            confirmButton = confirmButton,
            dismissButton = dismissButton,
            containerColor = HarmTheme.colors.surfaceContainerHigh,
            iconContentColor = HarmTheme.colors.secondary,
            titleContentColor = HarmTheme.colors.onSurface,
            textContentColor = HarmTheme.colors.onSurfaceVariant,
        )
    }

    /** A dialog with a question for user and ok/cancel buttons */
    @Composable
    fun HarmConfirmingDialog(
        dialogTitle: String,
        onDismissRequest: () -> Unit,
        onConfirmation: () -> Unit,
        modifier: Modifier = Modifier,
        @DrawableRes iconId: Int? = null,
        iconContentDescription: String? = null,
        dialogText: String? = null,
    ) {
        HarmCommonDialog(
            modifier = modifier,
            dialogTitle = dialogTitle,
            onDismissRequest = onDismissRequest,
            iconId = iconId,
            iconContentDescription = iconContentDescription,
            dismissButton = {
                HarmButton.HarmSecondaryTextButton(
                    text = stringResource(R.string.btn_dialog_cancel_text),
                    onClick = onDismissRequest
                )
            },
            confirmButton = {
                HarmButton.HarmPrimaryTextButton(
                    text = stringResource(R.string.btn_dialog_yes_text),
                    onClick = onConfirmation
                )
            },
            dialogContent = dialogText?.let {
                {
                    Text(
                        text = dialogText,
                        style = HarmTheme.typography.titleMedium,
                        textAlign = TextAlign.Start
                    )
                }
            }
        )
    }

    /** A dialog to set the first day of the month */
    @Composable
    fun HarmSetFirstDayMonthDialog(
        numberString: String,
        onNumberChanged: (String) -> Unit,
        onDismissRequest: () -> Unit,
        onConfirmation: () -> Unit,
        modifier: Modifier = Modifier,
        isError: Boolean = false,
        supportingText: String? = null,
        onTextFieldDoneAction: (() -> Unit) = {},
    ) {
        HarmCommonDialog(
            modifier = modifier,
            onDismissRequest = onDismissRequest,
            dialogContent = {
                Column {
                    Text(
                        text = stringResource(R.string.title_dialog_set_first_day_month),
                        style = HarmTheme.typography.bodyLarge,
                        color = HarmTheme.colors.onSurfaceContainer,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    HarmTextField.HarmBaseTextField(
                        value = numberString,
                        placeholder = stringResource(R.string.label_text_field_first_day_month),
                        label = stringResource(R.string.label_text_field_first_day_month),
                        onValueChange = onNumberChanged,
                        onDoneAction = onTextFieldDoneAction,
                        onlyNumbers = true,
                        isError = isError,
                        supportingText = supportingText
                    )
                }

            },
            dismissButton = {
                HarmButton.HarmSecondaryTextButton(
                    text = stringResource(R.string.btn_dialog_cancel_text),
                    onClick = onDismissRequest
                )
            },
            confirmButton = {
                HarmButton.HarmPrimaryTextButton(
                    text = stringResource(R.string.btn_save_text),
                    onClick = onConfirmation
                )
            }
        )
    }

    /** A dialog with a warning and one ok button */
    @Composable
    fun HarmWarningDialog(
        dialogTitle: String,
        onDismissRequest: () -> Unit,
        modifier: Modifier = Modifier,
        dialogText: String? = null,
    ) {
        HarmCommonDialog(
            modifier = modifier,
            dialogTitle = dialogTitle,
            onDismissRequest = onDismissRequest,
            iconId = R.drawable.ic_warring_24px,
            iconContentDescription = stringResource(R.string.ic_alert_dialog_desc),
            dismissButton = null,
            confirmButton = {
                HarmButton.HarmPrimaryTextButton(
                    text = stringResource(R.string.btn_dialog_ok_text),
                    onClick = onDismissRequest
                )
            },
            dialogContent = dialogText?.let {
                {
                    Text(
                        text = dialogText,
                        style = HarmTheme.typography.titleMedium,
                        textAlign = TextAlign.Start
                    )
                }
            }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun HarmConfirmingDialog_DarkPreview() {
    HarmTheme(darkTheme = true) {
        HarmDialog.HarmConfirmingDialog(
            dialogTitle = stringResource(R.string.title_dialog_delete_transaction),
            onDismissRequest = {},
            onConfirmation = {},
            iconId = R.drawable.ic_warring_24px,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun HarmConfirmingDialog_LightPreview() {
    HarmTheme(darkTheme = false) {
        HarmDialog.HarmConfirmingDialog(
            dialogTitle = stringResource(R.string.title_dialog_delete_transaction),
            onDismissRequest = {},
            onConfirmation = {},
            iconId = R.drawable.ic_warring_24px,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun HarmConfirmingDialogWithText_DarkPreview() {
    HarmTheme(darkTheme = true) {
        HarmDialog.HarmConfirmingDialog(
            dialogTitle = stringResource(R.string.title_dialog_exit),
            onDismissRequest = {},
            onConfirmation = {},
            iconId = R.drawable.ic_warring_24px,
            dialogText = stringResource(R.string.text_dialog_exit)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun HarmConfirmingDialogWithText_LightPreview() {
    HarmTheme(darkTheme = false) {
        HarmDialog.HarmConfirmingDialog(
            dialogTitle = stringResource(R.string.title_dialog_exit),
            onDismissRequest = {},
            onConfirmation = {},
            iconId = R.drawable.ic_warring_24px,
            dialogText = stringResource(R.string.text_dialog_exit)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun HarmSetFirstDayMonthDialog_DarkPreview() {
    HarmTheme(darkTheme = true) {
        HarmDialog.HarmSetFirstDayMonthDialog(
            numberString = "1",
            onNumberChanged = {},
            onConfirmation = {},
            onDismissRequest = {},
            isError = false,
            supportingText = null
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun HarmSetFirstDayMonthDialog_LightPreview() {
    HarmTheme(darkTheme = false) {
        HarmDialog.HarmSetFirstDayMonthDialog(
            numberString = "1",
            onNumberChanged = {},
            onConfirmation = {},
            onDismissRequest = {},
            isError = false,
            supportingText = null
        )
    }
}

@Suppress("detekt:MagicNumber")
@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun HarmSetFirstDayMonthDialog_DarkErrorPreview() {
    HarmTheme(darkTheme = true) {
        HarmDialog.HarmSetFirstDayMonthDialog(
            numberString = "90",
            onNumberChanged = {},
            onConfirmation = {},
            onDismissRequest = {},
            isError = true,
            supportingText = stringResource(
                R.string.error_incorrect_first_day_month_pattern,
                1,
                28
            )
        )
    }
}

@Suppress("detekt:MagicNumber")
@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun HarmSetFirstDayMonthDialog_LightErrorPreview() {
    HarmTheme(darkTheme = false) {
        HarmDialog.HarmSetFirstDayMonthDialog(
            numberString = "90",
            onNumberChanged = {},
            onConfirmation = {},
            onDismissRequest = {},
            isError = true,
            supportingText = stringResource(
                R.string.error_incorrect_first_day_month_pattern,
                1,
                28
            )
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun HarmWarningDialog_DarkPreview() {
    HarmTheme(darkTheme = true) {
        HarmDialog.HarmWarningDialog(
            dialogTitle = stringResource(R.string.title_dialog_transaction_save_error),
            onDismissRequest = {},
            dialogText = stringResource(R.string.text_dialog_transaction_save_error)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun HarmWarningDialog_LightPreview() {
    HarmTheme(darkTheme = false) {
        HarmDialog.HarmWarningDialog(
            dialogTitle = stringResource(R.string.title_dialog_transaction_save_error),
            onDismissRequest = {},
            dialogText = stringResource(R.string.text_dialog_transaction_save_error)
        )
    }
}
