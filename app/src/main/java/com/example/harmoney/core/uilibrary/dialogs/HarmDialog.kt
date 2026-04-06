package com.example.harmoney.core.uilibrary.dialogs

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.harmoney.R
import com.example.harmoney.core.uilibrary.buttons.HarmButton
import com.example.harmoney.ui.theme.HarmTheme

/**
 * - `HarmDialog` - base dialog with title, buttons and content
 * - `HarmConfirmingDialog` - A dialog with a question for user and ok/cancel buttons
 * - `HarmSetFirstDayMonthDialog` - A dialog to set the first day of the month
 */
object HarmDialog {
    /** Base dialog with title, buttons and content */
    @Composable
    fun HarmCommonDialog(
        onDismissRequest: () -> Unit,
        modifier: Modifier = Modifier,
        dialogTitle: String? = null,
        @DrawableRes iconId: Int? = null,
        iconContentDescription: String? = null,
        dismissButton: @Composable () -> Unit,
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
            containerColor = if (HarmTheme.colors.isDark) {
                HarmTheme.colors.surfaceContainer
            } else {
                HarmTheme.colors.surface
            },
            iconContentColor = HarmTheme.colors.onSurfaceContainer,
            titleContentColor = HarmTheme.colors.onSurfaceContainer,
            textContentColor = HarmTheme.colors.onSurfaceContainer,
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
                HarmButton.HarmSecondaryButton(
                    text = stringResource(R.string.btn_dialog_cancel_text),
                    onClick = onDismissRequest
                )
            },
            confirmButton = {
                HarmButton.HarmPrimaryButton(
                    text = stringResource(R.string.btn_dialog_ok_text),
                    onClick = onConfirmation
                )
            },
            dialogContent = dialogText?.let {
                {
                    Text(
                        text = dialogText,
                        style = HarmTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        )
    }

    /** A dialog to set the first day of the month */
    @Composable
    fun HarmSetFirstDayMonthDialog(
        number: Int,
        onNumberChanged: (Int) -> Unit,
        onDismissRequest: () -> Unit,
        onConfirmation: () -> Unit,
        modifier: Modifier = Modifier,
        isError: Boolean = false,
    ) {
        HarmCommonDialog(
            modifier = modifier,
            onDismissRequest = onDismissRequest,
            dialogContent = {
                // в будущем задать здесь TextField для ввода чисел
                Column {
                    Text(
                        text = stringResource(R.string.title_dialog_set_first_day_month),
                        style = HarmTheme.typography.bodyLarge,
                        color = HarmTheme.colors.onSurfaceContainer
                    )
                }

            },
            dismissButton = {
                HarmButton.HarmSecondaryButton(
                    text = stringResource(R.string.btn_dialog_cancel_text),
                    onClick = onDismissRequest
                )
            },
            confirmButton = {
                HarmButton.HarmPrimaryButton(
                    text = stringResource(R.string.btn_save_text),
                    onClick = onConfirmation
                )
            }
        )
    }
}
