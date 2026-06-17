package com.example.harmoney.core.uilibrary.textfields

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.harmoney.R
import com.example.harmoney.annotation.UiLibrary
import com.example.harmoney.core.uilibrary.date.HarmDate.HarmDatePickerModal
import com.example.harmoney.ui.theme.HarmTheme

/**
 * - `HarmBaseTextField` - base TextField for simple text or numeric content
 * - `HarmTextFieldWithDatePicker` - TextField with calendar logic
 */
@UiLibrary
object HarmTextField {
    /** Base TextField for simple text or numeric content */
    @Composable
    fun HarmBaseTextField(
        value: String,
        placeholder: String,
        label: String,
        onValueChange: (String) -> Unit,
        modifier: Modifier = Modifier,
        focusManager: FocusManager = LocalFocusManager.current,
        onlyNumbers: Boolean = false,
        readOnly: Boolean = false,
        isError: Boolean = false,
        supportingText: String? = null,
        onDoneAction: (() -> Unit) = {},
        trailingIcon: @Composable() (() -> Unit)? = null,
    ) {
        val isFocused = remember { mutableStateOf(false) }
        val colors = HarmTheme.colors
        val typography = HarmTheme.typography
        val contentColor = if (value.isEmpty()) {
            colors.onSurfaceContainerLow
        } else {
            colors.onSurfaceContainer
        }

        OutlinedTextField(
            modifier = modifier
                .onFocusChanged { focusState ->
                    isFocused.value = focusState.isFocused
                },
            value = value,
            onValueChange = { newValue ->
                if (onlyNumbers) {
                    var filteredValue = newValue.filter { it.isDigit() }
                    if (filteredValue.length > 1 && filteredValue.startsWith('0')) {
                        filteredValue = filteredValue.trimStart('0')
                    }
                    onValueChange(filteredValue)
                } else {
                    onValueChange(newValue)
                }
            },
            readOnly = readOnly,
            placeholder = {
                Text(
                    text = placeholder,
                    style = typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            textStyle = typography.bodyLarge,
            trailingIcon = trailingIcon,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colors.primary,
                unfocusedBorderColor = contentColor,
                errorBorderColor = colors.error,

                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,

                focusedLabelColor = colors.primary,
                unfocusedLabelColor = contentColor,
                disabledLabelColor = contentColor,
                errorLabelColor = colors.error,

                cursorColor = colors.primary,
                errorCursorColor = colors.primary,

                focusedLeadingIconColor = colors.onSurfaceContainer,
                unfocusedLeadingIconColor = contentColor,
                disabledLeadingIconColor = contentColor,
                errorLeadingIconColor = colors.error,

                focusedTrailingIconColor = colors.onSurfaceContainer,
                unfocusedTrailingIconColor = contentColor,
                disabledTrailingIconColor = contentColor,
                errorTrailingIconColor = colors.error,

                focusedTextColor = colors.onSurfaceContainer,
                unfocusedTextColor = colors.onSurfaceContainer,
                disabledTextColor = colors.onSurfaceContainer,
                errorTextColor = colors.error,

                focusedPlaceholderColor = colors.onSurfaceContainer,
                unfocusedPlaceholderColor = colors.onSurfaceContainerLow,
                disabledPlaceholderColor = colors.onSurfaceContainerLow,
                errorPlaceholderColor = colors.onSurfaceContainer,

                focusedSupportingTextColor = colors.onSurfaceContainer,
                unfocusedSupportingTextColor = colors.onSurfaceContainer,
                disabledSupportingTextColor = colors.onSurfaceContainer,
                errorSupportingTextColor = colors.error,

                selectionColors = TextSelectionColors(
                    handleColor = colors.primary,
                    backgroundColor = colors.primary
                ),
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = if (onlyNumbers) KeyboardType.Number else KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    onDoneAction()
                }
            ),
            shape = RoundedCornerShape(12.dp),
            label = {
                Column(
                    modifier = Modifier.background(Color.Transparent)
                ) {
                    Text(
                        text = if (!isFocused.value && value.isEmpty()) placeholder else label,
                        style = typography.labelMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            },
            maxLines = 2,
            isError = isError,
            supportingText = supportingText?.let {
                {
                    Text(
                        text = supportingText,
                        style = typography.labelMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        )
    }

    /** TextField with calendar logic */
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun HarmTextFieldWithDatePicker(
        selectedDateString: String,
        showModalDatePicker: Boolean,
        onDismiss: () -> Unit,
        onTextFieldTouch: () -> Unit,
        onDateSelected: (Long?) -> Unit,
        modifier: Modifier = Modifier,
        focusManager: FocusManager = LocalFocusManager.current,
        supportingText: String? = null,
        isError: Boolean = false,
    ) {
        HarmBaseTextField(
            modifier = modifier
                .fillMaxWidth()
                .pointerInput(selectedDateString) {
                    awaitEachGesture {
                        awaitFirstDown(pass = PointerEventPass.Initial)
                        val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                        if (upEvent != null) {
                            onTextFieldTouch()
                        }
                    }
                },
            value = selectedDateString,
            placeholder = stringResource(R.string.label_text_field_data),
            label = stringResource(R.string.label_text_field_data),
            readOnly = true,
            onValueChange = {},
            trailingIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_calendar_24px),
                    contentDescription = stringResource(R.string.ic_calendar_desc)
                )
            },
            focusManager = focusManager,
            isError = isError,
            supportingText = supportingText,
        )

        if (showModalDatePicker) {
            HarmDatePickerModal(
                onDateSelected = onDateSelected,
                onDismiss = onDismiss
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun HarmBaseTextField_EmptyDarkPreview() {
    HarmTheme(darkTheme = true) {
        HarmTextField.HarmBaseTextField(
            value = "",
            placeholder = "Введите сумму",
            label = "Сумма",
            onValueChange = {},
            trailingIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_calculator_24px),
                    contentDescription = null
                )
            }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun HarmBaseTextField_EmptyLightPreview() {
    HarmTheme(darkTheme = false) {
        HarmTextField.HarmBaseTextField(
            value = "",
            placeholder = "Введите сумму",
            label = "Сумма",
            onValueChange = {},
            trailingIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_calculator_24px),
                    contentDescription = null
                )
            }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun HarmBaseTextField_UnfocusedDarkPreview() {
    HarmTheme(darkTheme = true) {
        HarmTextField.HarmBaseTextField(
            value = "5000",
            placeholder = "Введите сумму",
            label = "Сумма",
            onValueChange = {},
            trailingIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_calculator_24px),
                    contentDescription = null
                )
            }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
private fun HarmBaseTextField_UnfocusedLightPreview() {
    HarmTheme(darkTheme = false) {
        HarmTextField.HarmBaseTextField(
            value = "5000",
            placeholder = "Введите сумму",
            label = "Сумма",
            onValueChange = {},
            trailingIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_calculator_24px),
                    contentDescription = null
                )
            }
        )
    }
}

@Suppress("detekt:MagicNumber")
@Preview(showBackground = true, backgroundColor = 0xFF201923)
@Composable
private fun HarmBaseTextField_ErrorDarkPreview() {
    HarmTheme(darkTheme = true) {
        HarmTextField.HarmBaseTextField(
            value = "90",
            placeholder = "Введите первый день месяца",
            label = "Первый день месяца",
            onValueChange = {},
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
private fun HarmBaseTextField_ErrorLightPreview() {
    HarmTheme(darkTheme = false) {
        HarmTextField.HarmBaseTextField(
            value = "90",
            placeholder = "Введите первый день месяца",
            label = "Первый день месяца",
            onValueChange = {},
            isError = true,
            supportingText = stringResource(
                R.string.error_incorrect_first_day_month_pattern,
                1,
                28
            )
        )
    }
}
