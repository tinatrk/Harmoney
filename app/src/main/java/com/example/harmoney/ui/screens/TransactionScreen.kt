package com.example.harmoney.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.example.harmoney.R
import com.example.harmoney.core.uilibrary.bottomsheets.HarmBottomSheet
import com.example.harmoney.core.uilibrary.buttons.HarmButton
import com.example.harmoney.core.uilibrary.calculator.HarmCalculator
import com.example.harmoney.core.uilibrary.dialogs.HarmDialog
import com.example.harmoney.core.uilibrary.menus.HarmMenu
import com.example.harmoney.core.uilibrary.textfields.HarmTextField
import com.example.harmoney.core.uilibrary.topbars.HarmTopBar
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.presentation.calculator.models.CalculatorEvent
import com.example.harmoney.presentation.calculator.models.CalculatorState
import com.example.harmoney.presentation.calculator.viewModel.CalculatorViewModel
import com.example.harmoney.presentation.models.MenuOption
import com.example.harmoney.presentation.sharedViewModel.SharedCategoryTypeViewModel
import com.example.harmoney.presentation.transaction.models.TransactionAction
import com.example.harmoney.presentation.transaction.models.TransactionAmountError
import com.example.harmoney.presentation.transaction.models.TransactionDateError
import com.example.harmoney.presentation.transaction.models.TransactionEvent
import com.example.harmoney.presentation.transaction.models.TransactionState
import com.example.harmoney.presentation.transaction.viewModel.TransactionViewModel
import com.example.harmoney.ui.components.ScreenWithCategoryTypeTabs
import com.example.harmoney.ui.mappers.CategoryIconUiMapper.toDrawableRes
import com.example.harmoney.ui.other.PreviewData
import com.example.harmoney.ui.theme.HarmTheme
import kotlinx.collections.immutable.toImmutableList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreen(
    sharedCategoryTypeViewModel: SharedCategoryTypeViewModel,
    viewModel: TransactionViewModel,
    calculatorViewModel: CalculatorViewModel,
    onBackClick: () -> Unit,
    onNavigateToCategoryScreen: () -> Unit,
) {
    val categoryType by sharedCategoryTypeViewModel.selectedCategoryType
        .collectAsStateWithLifecycle()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val calculatorState by calculatorViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(categoryType) {
        viewModel.obtainEvent(TransactionEvent.OnTabClick(categoryType))
    }

    LaunchedEffect(calculatorState.result) {
        if (state.isCalculatorOpen) {
            viewModel.obtainEvent(
                TransactionEvent.OnAmountChanged(calculatorState.result)
            )
        }
    }

    LaunchedEffect(state.isCalculatorOpen) {
        if (state.isCalculatorOpen) {
            calculatorViewModel.obtainEvent(
                CalculatorEvent.OnOpenCalculator(state.amountInLocalCurrency)
            )
        } else {
            calculatorViewModel.obtainEvent(CalculatorEvent.OnCloseCalculator)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.action
            .flowWithLifecycle(lifecycle, minActiveState = Lifecycle.State.STARTED)
            .collect { act ->
                when (act) {
                    TransactionAction.NavigateBack -> onBackClick()
                    is TransactionAction.NavigateToCategoryScreen -> {
                        onNavigateToCategoryScreen()
                    }

                    else -> {}
                }
            }
    }

    BackHandler(!state.isTransactionNotSavedDialogOpened) {
        viewModel.obtainEvent(TransactionEvent.OnBackClick)
    }

    TransactionScreen(
        state = state,
        onEvent = viewModel::obtainEvent,
        onCategoryTypeChanged = sharedCategoryTypeViewModel::categoryTypeChanged,
        calculatorState = calculatorState,
        onCalculatorEvent = calculatorViewModel::obtainEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreen(
    state: TransactionState,
    onEvent: (TransactionEvent) -> Unit,
    calculatorState: CalculatorState,
    onCalculatorEvent: (CalculatorEvent) -> Unit,
    onCategoryTypeChanged: (CategoryType) -> Unit,
) {
    Scaffold(
        topBar = {
            HarmTopBar.HarmSimpleTopBar(
                title = stringResource(
                    if (state.isCreateTransactionScreen) {
                        R.string.title_top_app_bar_create_transaction
                    } else {
                        R.string.title_top_app_bar_edit_transaction
                    }
                ),
                onNavigationIconClick = { onEvent(TransactionEvent.OnBackClick) },
            )
        },
        containerColor = HarmTheme.colors.surface
    ) { paddingValues ->
        ScreenWithCategoryTypeTabs(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues),
            tabs = CategoryType.entries.toImmutableList(),
            selectedTabIndex = state.selectedTabIndex,
            onTabClick = { categoryType -> onCategoryTypeChanged(categoryType) }
        ) {
            TransactionContent(
                state = state,
                onEvent = onEvent,
                calculatorState = calculatorState,
                onCalculatorEvent = onCalculatorEvent,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionContent(
    state: TransactionState,
    onEvent: (TransactionEvent) -> Unit,
    calculatorState: CalculatorState,
    onCalculatorEvent: (CalculatorEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        TextFields(
            state = state,
            onEvent = onEvent,
            calculatorState = calculatorState,
            onCalculatorEvent = onCalculatorEvent,
        )

        Spacer(modifier = Modifier.height(16.dp))
        CategoryList(
            modifier = Modifier.weight(1f),
            state = state,
            onEvent = onEvent,
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center
        ) {
            if (!state.isCreateTransactionScreen) {
                HarmButton.HarmDangerousButton(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.btn_delete_text),
                    onClick = { onEvent(TransactionEvent.OnDeleteClick) }
                )
                Spacer(modifier = Modifier.width(16.dp))
            }
            HarmButton.HarmPrimaryButton(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.btn_save_text),
                onClick = { onEvent(TransactionEvent.OnSaveClick) }
            )
        }
    }

    Dialogs(state = state, onEvent = onEvent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TextFields(
    state: TransactionState,
    onEvent: (TransactionEvent) -> Unit,
    calculatorState: CalculatorState,
    onCalculatorEvent: (CalculatorEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    val dateSupportingText = when (val error = state.dateError) {
        is TransactionDateError.None -> ""
        is TransactionDateError.OutOfRange -> {
            stringResource(
                R.string.error_incorrect_date,
                error.firstDay,
                error.lastDay
            )
        }
    }

    val amountSupportingText = when (state.amountError) {
        is TransactionAmountError.None -> ""
        is TransactionAmountError.Empty -> stringResource(R.string.error_empty_field)
        is TransactionAmountError.IncorrectInput -> {
            stringResource(R.string.error_incorrect_amount)
        }
    }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {

        // Amount
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HarmTextField.HarmBaseTextField(
                    modifier = Modifier
                        .weight(1f)
                        .pointerInput(calculatorState.equation) {
                            awaitEachGesture {
                                awaitFirstDown(pass = PointerEventPass.Initial)
                                val upEvent =
                                    waitForUpOrCancellation(pass = PointerEventPass.Initial)
                                if (upEvent != null) {
                                    onEvent(TransactionEvent.OnCalculatorOpen)
                                }
                            }
                        },
                    value = state.amountInLocalCurrency,
                    placeholder = stringResource(R.string.label_text_field_amount),
                    label = stringResource(R.string.label_text_field_amount),
                    onValueChange = {},
                    onlyNumbers = true,
                    readOnly = true,
                    isError = state.amountError != TransactionAmountError.None,
                    supportingText = amountSupportingText,
                    focusManager = focusManager,
                )

                Spacer(modifier = Modifier.width(4.dp))
                HarmMenu.HarmDropdownMenu(
                    expanded = state.isCurrencyMenuOpened,
                    menuOptions = Currency.entries.sortedBy { it.code }.map { currency ->
                        MenuOption(text = currency.code) {
                            onEvent(TransactionEvent.OnCurrencyChanged(currency))
                        }
                    }.toImmutableList(),
                    onDismissRequest = { onEvent(TransactionEvent.OnCurrencyDismiss) }
                ) {
                    Row(
                        modifier = Modifier
                            .wrapContentWidth()
                            .clickable { onEvent(TransactionEvent.OnCurrencyClick) },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = state.localCurrency.symbol,
                            style = HarmTheme.typography.bodyLarge,
                            color = HarmTheme.colors.onSurface
                        )
                        Icon(
                            painter = if (state.isCurrencyMenuOpened) {
                                painterResource(R.drawable.ic_menu_arrow_up_24)
                            } else {
                                painterResource(R.drawable.ic_menu_arrow_down_24)
                            },
                            contentDescription = if (state.isCurrencyMenuOpened) {
                                stringResource(R.string.ic_menu_arrow_up_desc)
                            } else {
                                stringResource(R.string.ic_menu_arrow_down_desc)
                            },
                            tint = HarmTheme.colors.onSurface,
                        )
                    }
                }
            }

            if (state.isUsedCurrencyExchange) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "=",
                    style = HarmTheme.typography.bodyLarge,
                    color = HarmTheme.colors.onSurface
                )

                Spacer(modifier = Modifier.width(8.dp))
                Row(
                    modifier = Modifier.wrapContentWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = state.amountInGlobalCurrency,
                        style = HarmTheme.typography.bodyLarge,
                        color = HarmTheme.colors.onSurface
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = state.globalCurrency.symbol,
                        style = HarmTheme.typography.bodyLarge,
                        color = HarmTheme.colors.onSurface
                    )
                }
            }
        }

        // Date
        Spacer(modifier = Modifier.height(8.dp))
        HarmTextField.HarmTextFieldWithDatePicker(
            selectedDateString = state.selectedDate,
            showModalDatePicker = state.isDatePickerOpened,
            onDismiss = {
                onEvent(TransactionEvent.OnDateDialogDismiss)
                focusManager.clearFocus()
            },
            onTextFieldTouch = { onEvent(TransactionEvent.OnDateDialogOpen) },
            onDateSelected = { newDateMillis ->
                onEvent(TransactionEvent.OnDateDialogConfirm(newDateMillis))
                focusManager.clearFocus()
            },
            isError = state.dateError != TransactionDateError.None,
            supportingText = dateSupportingText,
            focusManager = focusManager
        )

        // Note
        Spacer(modifier = Modifier.height(8.dp))
        HarmTextField.HarmBaseTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.note,
            placeholder = stringResource(R.string.label_text_field_note),
            label = stringResource(R.string.label_text_field_note),
            onValueChange = { newNote -> onEvent(TransactionEvent.OnNoteChanged(newNote)) },
        )
    }

    if (state.isCalculatorOpen) {
        HarmBottomSheet(
            onDismissRequest = {
                onEvent(TransactionEvent.OnCalculatorDismiss)
                // очищаем фокус textField c суммой
                focusManager.clearFocus()
            },
            isNeedScrim = false,
            skipPartiallyExpanded = true
        ) {
            HarmCalculator(
                state = calculatorState,
                onEvent = onCalculatorEvent,
                onCalculateClick = {
                    onEvent(TransactionEvent.OnCalculatorDismiss)
                    // очищаем фокус textField c суммой
                    focusManager.clearFocus()
                },
            )
        }
    }
}

@OptIn(ExperimentalStdlibApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CategoryList(
    state: TransactionState,
    onEvent: (TransactionEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberLazyGridState()

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.title_category),
            style = HarmTheme.typography.bodyLarge,
            color = if (state.isCategoryError) {
                HarmTheme.colors.error
            } else {
                HarmTheme.colors.onSurface
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (state.selectedCategory != null) {
            HarmButton.HarmCircularIconButtonWithTitle(
                iconRes = state.selectedCategory.icon.icon.toDrawableRes(),
                contentDescription = stringResource(
                    R.string.ic_category_desc, state.selectedCategory.name
                ),
                iconBackground = Color(state.selectedCategory.icon.color.background),
                iconTitle = state.selectedCategory.name,
                selected = false,
                onClick = { onEvent(TransactionEvent.OnCategoriesBottomSheetOpened) }
            )
        } else {
            HarmButton.HarmCircularIconButtonWithTitle(
                iconRes = R.drawable.ic_add_24px,
                contentDescription = stringResource(R.string.ic_add_category_desc),
                iconBackground = HarmTheme.colors.primary,
                iconTint = HarmTheme.colors.onPrimary,
                iconTitle = stringResource(R.string.btn_create_text),
                selected = false,
                onClick = { onEvent(TransactionEvent.OnCreateCategoryClick) }
            )
        }

        if (state.isCategoriesBottomSheetOpened) {
            HarmBottomSheet(
                onDismissRequest = { onEvent(TransactionEvent.OnCategoriesBottomSheetDismiss) },
                skipPartiallyExpanded = true,
                nestedScrollableState = scrollState
            ) {
                LazyVerticalGrid(
                    state = scrollState,
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(count = 1) {
                        HarmButton.HarmCircularIconButtonWithTitle(
                            iconRes = R.drawable.ic_add_24px,
                            contentDescription = stringResource(R.string.ic_add_category_desc),
                            iconBackground = HarmTheme.colors.primary,
                            iconTint = HarmTheme.colors.onPrimary,
                            iconTitle = stringResource(R.string.btn_create_text),
                            selected = false,
                            onClick = { onEvent(TransactionEvent.OnCreateCategoryClick) }
                        )
                    }
                    items(state.categories) { category ->
                        HarmButton.HarmCircularIconButtonWithTitle(
                            iconRes = category.icon.icon.toDrawableRes(),
                            contentDescription = stringResource(
                                R.string.ic_category_desc, category.name
                            ),
                            iconBackground = Color(category.icon.color.background),
                            iconTitle = category.name,
                            selected = state.selectedCategory?.id == category.id,
                            onClick = {
                                onEvent(TransactionEvent.OnCategoryClick(category.id))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Dialogs(
    state: TransactionState,
    onEvent: (TransactionEvent) -> Unit
) {
    if (state.isTransactionNotSavedDialogOpened) {
        HarmDialog.HarmConfirmingDialog(
            dialogTitle = stringResource(R.string.title_dialog_exit),
            dialogText = stringResource(R.string.text_dialog_exit),
            iconId = R.drawable.ic_warring_24px,
            iconContentDescription = stringResource(R.string.ic_alert_dialog_desc),
            onConfirmation = { onEvent(TransactionEvent.OnBackDialogConfirm) },
            onDismissRequest = { onEvent(TransactionEvent.OnBackDialogDismiss) }
        )
    }

    if (state.isSaveTransactionErrorDialogOpened) {
        HarmDialog.HarmWarningDialog(
            dialogTitle = stringResource(R.string.title_dialog_transaction_save_error),
            dialogText = stringResource(R.string.text_dialog_saving_error),
            onDismissRequest = { onEvent(TransactionEvent.OnSaveDialogDismiss) }
        )
    }

    if (state.isTransactionDeleteDialogOpened) {
        HarmDialog.HarmConfirmingDialog(
            dialogTitle = stringResource(R.string.title_dialog_delete_transaction),
            iconId = R.drawable.ic_warring_24px,
            iconContentDescription = stringResource(R.string.ic_alert_dialog_desc),
            onConfirmation = { onEvent(TransactionEvent.OnDeleteDialogConfirm) },
            onDismissRequest = { onEvent(TransactionEvent.OnDeleteDialogDismiss) }
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun TransactionListScreen_CreateEmptyWithoutCategoriesDarkPreview() {
    HarmTheme(darkTheme = true) {
        TransactionScreen(
            state = TransactionState(
                isCreateTransactionScreen = true,
                selectedDate = "11 Мая 2026",
                amountInLocalCurrency = "0",
            ),
            onEvent = {},
            calculatorState = CalculatorState(),
            onCalculatorEvent = {},
            onCategoryTypeChanged = {}
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun TransactionListScreen_CreateEmptyWithoutCategoriesLightPreview() {
    HarmTheme(darkTheme = false) {
        TransactionScreen(
            state = TransactionState(
                isCreateTransactionScreen = true,
                selectedDate = "11 Мая 2026",
                amountInLocalCurrency = "0",
            ),
            onEvent = {},
            calculatorState = CalculatorState(),
            onCalculatorEvent = {},
            onCategoryTypeChanged = {}
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun TransactionListScreen_CreateWithNoteDarkPreview() {
    HarmTheme(darkTheme = true) {
        TransactionScreen(
            state = TransactionState(
                isCreateTransactionScreen = true,
                selectedDate = "11 Мая 2026",
                amountInLocalCurrency = "1 000",
                categories = PreviewData.getExpensesCategories(),
                selectedCategory = PreviewData.getExpensesCategories().first(),
                note = "Новый тариф"
            ),
            onEvent = {},
            calculatorState = CalculatorState(),
            onCalculatorEvent = {},
            onCategoryTypeChanged = {}
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun TransactionListScreen_CreateWithNoteLightPreview() {
    HarmTheme(darkTheme = false) {
        TransactionScreen(
            state = TransactionState(
                isCreateTransactionScreen = true,
                selectedDate = "11 Мая 2026",
                amountInLocalCurrency = "1 000",
                categories = PreviewData.getExpensesCategories(),
                selectedCategory = PreviewData.getExpensesCategories().first(),
                note = "Новый тариф"
            ),
            onEvent = {},
            calculatorState = CalculatorState(),
            onCalculatorEvent = {},
            onCategoryTypeChanged = {}
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun TransactionListScreen_CreateWithErrorDarkPreview() {
    HarmTheme(darkTheme = true) {
        TransactionScreen(
            state = TransactionState(
                isCreateTransactionScreen = true,
                selectedDate = "11 Апреля 2026",
                dateError = TransactionDateError
                    .OutOfRange(firstDay = "01 Мая 2026", lastDay = "31 Мая 2026"),
                amountInLocalCurrency = "0",
                amountError = TransactionAmountError.IncorrectInput,
                note = "Новый тариф",
                isCategoryError = true
            ),
            onEvent = {},
            calculatorState = CalculatorState(),
            onCalculatorEvent = {},
            onCategoryTypeChanged = {}
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun TransactionListScreen_CreateWithErrorLightPreview() {
    HarmTheme(darkTheme = false) {
        TransactionScreen(
            state = TransactionState(
                isCreateTransactionScreen = true,
                selectedDate = "11 Апреля 2026",
                dateError = TransactionDateError
                    .OutOfRange(firstDay = "01 Мая 2026", lastDay = "31 Мая 2026"),
                amountInLocalCurrency = "0",
                amountError = TransactionAmountError.IncorrectInput,
                note = "Новый тариф",
                isCategoryError = true
            ),
            onEvent = {},
            calculatorState = CalculatorState(),
            onCalculatorEvent = {},
            onCategoryTypeChanged = {}
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun TransactionListScreen_CreateWithCurrencyMenuDarkPreview() {
    HarmTheme(darkTheme = true) {
        TransactionScreen(
            state = TransactionState(
                isCreateTransactionScreen = true,
                selectedDate = "11 Мая 2026",
                amountInLocalCurrency = "1 000",
                isCurrencyMenuOpened = true,
                note = "Новый тариф",
                categories = PreviewData.getExpensesCategories(),
                selectedCategory = PreviewData.getExpensesCategories().first(),
            ),
            onEvent = {},
            calculatorState = CalculatorState(),
            onCalculatorEvent = {},
            onCategoryTypeChanged = {}
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun TransactionListScreen_CreateWithCurrencyMenuLightPreview() {
    HarmTheme(darkTheme = false) {
        TransactionScreen(
            state = TransactionState(
                isCreateTransactionScreen = true,
                selectedDate = "11 Мая 2026",
                amountInLocalCurrency = "1 000",
                isCurrencyMenuOpened = true,
                note = "Новый тариф",
                categories = PreviewData.getExpensesCategories(),
                selectedCategory = PreviewData.getExpensesCategories().first(),
            ),
            onEvent = {},
            calculatorState = CalculatorState(),
            onCalculatorEvent = {},
            onCategoryTypeChanged = {}
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun TransactionListScreen_CreateWithCurrencyExchangeDarkPreview() {
    HarmTheme(darkTheme = true) {
        TransactionScreen(
            state = TransactionState(
                isCreateTransactionScreen = true,
                selectedDate = "11 Мая 2026",
                amountInLocalCurrency = "1 000",
                localCurrency = Currency.USD,
                amountInGlobalCurrency = "80 000",
                globalCurrency = Currency.RUB,
                isUsedCurrencyExchange = true,
                note = "Новый тариф",
                categories = PreviewData.getExpensesCategories(),
                selectedCategory = PreviewData.getExpensesCategories().first(),
            ),
            onEvent = {},
            calculatorState = CalculatorState(),
            onCalculatorEvent = {},
            onCategoryTypeChanged = {}
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun TransactionListScreen_CreateWithCurrencyExchangeLightPreview() {
    HarmTheme(darkTheme = false) {
        TransactionScreen(
            state = TransactionState(
                isCreateTransactionScreen = true,
                selectedDate = "11 Мая 2026",
                amountInLocalCurrency = "1 000",
                localCurrency = Currency.USD,
                amountInGlobalCurrency = "80 000",
                globalCurrency = Currency.RUB,
                isUsedCurrencyExchange = true,
                note = "Новый тариф",
                categories = PreviewData.getExpensesCategories(),
                selectedCategory = PreviewData.getExpensesCategories().first(),
            ),
            onEvent = {},
            calculatorState = CalculatorState(),
            onCalculatorEvent = {},
            onCategoryTypeChanged = {}
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun TransactionListScreen_OpenEmptyDarkPreview() {
    HarmTheme(darkTheme = true) {
        TransactionScreen(
            state = TransactionState(
                isCreateTransactionScreen = false,
                selectedDate = "07 Мая 2026",
                amountInLocalCurrency = "3 000",
                note = "Ужин",
                categories = PreviewData.getExpensesCategories(),
                selectedCategory = PreviewData.getExpensesCategories().last(),
            ),
            onEvent = {},
            calculatorState = CalculatorState(),
            onCalculatorEvent = {},
            onCategoryTypeChanged = {}
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun TransactionListScreen_OpenEmptyLightPreview() {
    HarmTheme(darkTheme = false) {
        TransactionScreen(
            state = TransactionState(
                isCreateTransactionScreen = false,
                selectedDate = "07 Мая 2026",
                amountInLocalCurrency = "3 000",
                note = "Ужин",
                categories = PreviewData.getExpensesCategories(),
                selectedCategory = PreviewData.getExpensesCategories().last(),
            ),
            onEvent = {},
            calculatorState = CalculatorState(),
            onCalculatorEvent = {},
            onCategoryTypeChanged = {}
        )
    }
}
