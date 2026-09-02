package com.example.harmoney.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
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
import com.example.harmoney.core.uilibrary.dialogs.HarmDialog
import com.example.harmoney.core.uilibrary.textfields.HarmTextField
import com.example.harmoney.core.uilibrary.topbars.HarmTopBar
import com.example.harmoney.domain.models.CategoryColors
import com.example.harmoney.domain.models.CategoryIcons
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.presentation.category.models.CategoryAction
import com.example.harmoney.presentation.category.models.CategoryEvent
import com.example.harmoney.presentation.category.models.CategoryIconSubType
import com.example.harmoney.presentation.category.models.CategoryNameError
import com.example.harmoney.presentation.category.models.CategoryState
import com.example.harmoney.presentation.category.viewModel.CategoryViewModel
import com.example.harmoney.ui.mappers.CategoryIconSubTypeUiMapper.toStringRes
import com.example.harmoney.ui.mappers.CategoryIconUiMapper.toDrawableRes
import com.example.harmoney.ui.mappers.CategoryTypeUiMapper.toStringRes
import com.example.harmoney.ui.theme.HarmTheme

@Composable
fun CategoryScreen(
    viewModel: CategoryViewModel,
    onBackClick: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.action
            .flowWithLifecycle(lifecycle, minActiveState = Lifecycle.State.STARTED)
            .collect { act ->
                when (act) {
                    is CategoryAction.NavigateBack -> onBackClick()

                    is CategoryAction.CreateCategoryError -> {
                        Toast.makeText(
                            context,
                            context.getString(R.string.toast_category_create_error),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is CategoryAction.UpdateCategoryError -> {
                        Toast.makeText(
                            context,
                            context.getString(R.string.toast_category_update_error),
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                    is CategoryAction.DeleteCategoryError -> {
                        Toast.makeText(
                            context,
                            context.getString(R.string.toast_category_delete_error),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is CategoryAction.SaveCategoryError -> {
                        Toast.makeText(
                            context,
                            context.getString(R.string.toast_saving_error),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is CategoryAction.DataLoadingError -> {
                        Toast.makeText(
                            context,
                            context.getString(R.string.toast_data_loading_error),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is CategoryAction.CheckingCategoryAlreadyExistsError -> {
                        Toast.makeText(
                            context,
                            context.getString(R.string.error_something_went_wrong),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is CategoryAction.CreatedSuccessfully -> {
                        Toast.makeText(
                            context,
                            context.getString(
                                R.string.toast_category_created_successfully,
                                act.name
                            ),
                            Toast.LENGTH_SHORT
                        ).show()

                        onBackClick()
                    }

                    is CategoryAction.UpdatedSuccessfully -> {
                        Toast.makeText(
                            context,
                            context.getString(R.string.toast_updated_successfully),
                            Toast.LENGTH_SHORT
                        ).show()

                        onBackClick()
                    }

                    is CategoryAction.DeletedSuccessfully -> {
                        Toast.makeText(
                            context,
                            context.getString(
                                R.string.toast_category_deletion_successfully,
                                act.name
                            ),
                            Toast.LENGTH_SHORT
                        ).show()

                        onBackClick()
                    }

                    null -> Unit
                }
            }
    }

    BackHandler(!state.isCategoryNotSavedDialogOpened) {
        viewModel.obtainEvent(CategoryEvent.OnBackClick)
    }

    CategoryScreen(
        state = state,
        onEvent = viewModel::obtainEvent
    )
}

@Composable
fun CategoryScreen(
    state: CategoryState,
    onEvent: (CategoryEvent) -> Unit
) {
    Scaffold(
        topBar = {
            HarmTopBar.HarmSimpleTopBar(
                title = stringResource(
                    if (state.isCreateCategoryScreen) {
                        R.string.title_top_app_bar_create_category
                    } else {
                        R.string.title_top_app_bar_edit_category
                    }
                ),
                onNavigationIconClick = { onEvent(CategoryEvent.OnBackClick) },
            )
        },
        containerColor = HarmTheme.colors.surface,
    ) { paddingValues ->
        CategoryContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            state = state,
            onEvent = onEvent,
        )
    }
}

@Composable
fun CategoryContent(
    state: CategoryState,
    onEvent: (CategoryEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HarmTheme.colors.surface)
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        IconAndName(
            state = state,
            onEvent = onEvent,
            modifier = Modifier.fillMaxWidth(),
            focusManager = focusManager
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CategoryType.entries.forEach { type ->
                HarmButton.HarmCheckableIconWithTitle(
                    modifier = Modifier.weight(1f),
                    title = stringResource(type.toStringRes()),
                    checked = state.selectedCategoryType.id == type.id,
                    onCheckChanged = {
                        focusManager.clearFocus()
                        onEvent(CategoryEvent.OnChangeCategoryTypeClick(type))
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        ColorList(
            state = state,
            onEvent = onEvent,
            modifier = Modifier.weight(1f),
            focusManager = focusManager
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            if (!state.isCreateCategoryScreen) {
                HarmButton.HarmDangerousButton(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.btn_delete_text),
                    onClick = { onEvent(CategoryEvent.OnDeleteClick) },
                    enabled = state.isDataReadyForEditing
                )
                Spacer(modifier = Modifier.width(16.dp))
            }
            HarmButton.HarmPrimaryButton(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.btn_save_text),
                onClick = { onEvent(CategoryEvent.OnSaveClick) },
                enabled = state.isDataReadyForEditing
            )
        }
    }

    Dialogs(state = state, onEvent = onEvent)
}

@Composable
fun IconAndName(
    state: CategoryState,
    onEvent: (CategoryEvent) -> Unit,
    focusManager: FocusManager,
    modifier: Modifier = Modifier,
) {
    val supportingText = when (state.categoryNameError) {
        is CategoryNameError.Empty -> stringResource(R.string.error_empty_field)
        is CategoryNameError.AlreadyExists -> {
            stringResource(R.string.error_category_name_already_exists)
        }

        is CategoryNameError.CheckFailed -> stringResource(R.string.error_something_went_wrong)

        is CategoryNameError.None -> ""
    }
    val scrollState = rememberLazyGridState()

    Row(horizontalArrangement = Arrangement.Center) {
        Box(
            modifier = Modifier
                .padding(top = 8.dp)
                .height(56.dp),
            contentAlignment = Alignment.Center
        ) {
            HarmButton.HarmCircularIconButton(
                iconRes = state.selectedIcon.toDrawableRes(),
                contentDescription = stringResource(R.string.ic_change_category_icon_desc),
                iconBackground = Color(state.selectedColor.background),
                onClick = {
                    focusManager.clearFocus()
                    onEvent(CategoryEvent.OnOpenIconsBottomSheetClick)
                }
            )
        }
        Spacer(modifier = Modifier.width(12.dp))

        HarmTextField.HarmBaseTextField(
            value = state.categoryName,
            placeholder = stringResource(R.string.label_text_field_category_name),
            label = stringResource(R.string.label_text_field_category_name),
            onValueChange = { newName -> onEvent(CategoryEvent.OnCategoryNameChanged(newName)) },
            isError = state.categoryNameError !is CategoryNameError.None,
            supportingText = supportingText,
            readOnly = !state.isDataReadyForEditing,
            focusManager = focusManager,
        )
    }

    if (state.isIconsBottomSheetOpened) {
        HarmBottomSheet(
            isNeedScrim = true,
            skipPartiallyExpanded = true,
            nestedScrollableState = scrollState,
            onDismissRequest = { onEvent(CategoryEvent.OnIconsBottomSheetDismiss) }
        ) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(CategoryIconSubType.entries) { subList ->
                    Text(
                        text = stringResource(subList.toStringRes()),
                        style = HarmTheme.typography.bodyLarge,
                        color = HarmTheme.colors.onSurface
                    )
                    Spacer(modifier.height(12.dp))
                    FlowRow(modifier = Modifier.fillMaxWidth()) {
                        subList.icons.forEach { icon ->
                            HarmButton.HarmCircularIconButton(
                                iconRes = icon.toDrawableRes(),
                                contentDescription = stringResource(
                                    R.string.ic_select_category_icon_for_category_desc
                                ),
                                iconBackground = if (icon.id != state.selectedIcon.id) {
                                    HarmTheme.colors.surfaceContainerHighest
                                } else {
                                    HarmTheme.colors.primary
                                },
                                iconTint = if (icon.id != state.selectedIcon.id) {
                                    HarmTheme.colors.onSurface
                                } else {
                                    HarmTheme.colors.onPrimary
                                },
                                onClick = { onEvent(CategoryEvent.OnIconClick(icon)) }
                            )
                        }
                    }
                    Spacer(modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun ColorList(
    state: CategoryState,
    onEvent: (CategoryEvent) -> Unit,
    focusManager: FocusManager,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.title_color),
            style = HarmTheme.typography.bodyLarge,
            color = HarmTheme.colors.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyVerticalGrid(
            columns = GridCells.FixedSize(size = 32.dp),
            horizontalArrangement =
                Arrangement.spacedBy(space = 16.dp, alignment = Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(CategoryColors.entries) { color ->
                HarmButton.HarmCircularIconButton(
                    modifier = Modifier.size(32.dp),
                    iconRes = if (state.selectedColor.id == color.id) {
                        R.drawable.ic_ok_24px
                    } else {
                        null
                    },
                    contentDescription = if (state.selectedColor.id == color.id) {
                        stringResource(R.string.ic_selected_color_for_category_icon_desc)
                    } else {
                        stringResource(R.string.ic_select_color_for_category_icon_desc)
                    },
                    iconBackground = Color(color.background),
                    onClick = {
                        focusManager.clearFocus()
                        onEvent(CategoryEvent.OnColorClick(color))
                    }
                )
            }
        }
    }
}

@Composable
fun Dialogs(
    state: CategoryState,
    onEvent: (CategoryEvent) -> Unit,
) {
    if (state.isCategoryNotSavedDialogOpened) {
        HarmDialog.HarmConfirmingDialog(
            dialogTitle = stringResource(R.string.title_dialog_exit),
            dialogText = stringResource(R.string.text_dialog_exit),
            iconId = R.drawable.ic_warring_24px,
            iconContentDescription = stringResource(R.string.ic_alert_dialog_desc),
            onConfirmation = { onEvent(CategoryEvent.OnBackDialogConfirm) },
            onDismissRequest = { onEvent(CategoryEvent.OnBackDialogDismiss) }
        )
    }

    if (state.isSaveCategoryErrorDialogOpened) {
        HarmDialog.HarmWarningDialog(
            dialogTitle = stringResource(R.string.title_dialog_category_save_error),
            dialogText = stringResource(R.string.text_dialog_saving_error),
            onDismissRequest = { onEvent(CategoryEvent.OnSaveDialogDismiss) }
        )
    }

    if (state.isCategoryDeleteDialogOpened) {
        HarmDialog.HarmConfirmingDialog(
            dialogTitle = stringResource(
                R.string.title_dialog_delete_category,
                state.initCategoryName
            ),
            iconId = R.drawable.ic_warring_24px,
            iconContentDescription = stringResource(R.string.ic_alert_dialog_desc),
            onConfirmation = { onEvent(CategoryEvent.OnDeleteDialogConfirm) },
            onDismissRequest = { onEvent(CategoryEvent.OnDeleteDialogDismiss) }
        )
    }

    if (state.isDataLoadingErrorDialogOpened) {
        HarmDialog.HarmWarningDialog(
            dialogTitle = stringResource(R.string.title_dialog_category_loading_error),
            dialogText = stringResource(R.string.text_dialog_data_loading_error),
            onDismissRequest = { onEvent(CategoryEvent.OnDataLoadingErrorDialogConfirm) }
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CategoryScreen_CreateDarkPreview() {
    HarmTheme(darkTheme = true) {
        CategoryScreen(
            state = CategoryState(isCreateCategoryScreen = true),
            onEvent = {},
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CategoryScreen_CreateLightPreview() {
    HarmTheme(darkTheme = false) {
        CategoryScreen(
            state = CategoryState(isCreateCategoryScreen = true),
            onEvent = {},
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CategoryScreen_EditDarkPreview() {
    HarmTheme(darkTheme = true) {
        CategoryScreen(
            state = CategoryState(
                isCreateCategoryScreen = false,
                categoryName = "Подарки",
                selectedIcon = CategoryIcons.IC_GIFT,
                selectedColor = CategoryColors.PINK_T75,
            ),
            onEvent = {},
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CategoryScreen_EditLightPreview() {
    HarmTheme(darkTheme = false) {
        CategoryScreen(
            state = CategoryState(
                isCreateCategoryScreen = false,
                categoryName = "Подарки",
                selectedIcon = CategoryIcons.IC_GIFT,
                selectedColor = CategoryColors.PINK_T75,
            ),
            onEvent = {},
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CategoryScreen_IconBottomSheetDarkPreview() {
    HarmTheme(darkTheme = true) {
        CategoryScreen(
            state = CategoryState(
                isCreateCategoryScreen = false,
                categoryName = "Подарки",
                selectedIcon = CategoryIcons.IC_GIFT,
                selectedColor = CategoryColors.PINK_T75,
                isIconsBottomSheetOpened = true
            ),
            onEvent = {},
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CategoryScreen_IconBottomSheetLightPreview() {
    HarmTheme(darkTheme = false) {
        CategoryScreen(
            state = CategoryState(
                isCreateCategoryScreen = false,
                categoryName = "Подарки",
                selectedIcon = CategoryIcons.IC_GIFT,
                selectedColor = CategoryColors.PINK_T75,
                isIconsBottomSheetOpened = true
            ),
            onEvent = {},
        )
    }
}
