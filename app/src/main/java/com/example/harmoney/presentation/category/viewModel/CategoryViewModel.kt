package com.example.harmoney.presentation.category.viewModel

import androidx.lifecycle.viewModelScope
import com.example.harmoney.base.BaseViewModel
import com.example.harmoney.core.session.SessionStateHolder
import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.category.api.useCase.AddCategoryUseCase
import com.example.harmoney.domain.category.api.useCase.CheckCategoryAlreadyExistsUseCase
import com.example.harmoney.domain.category.api.useCase.DeleteCategoryUseCase
import com.example.harmoney.domain.category.api.useCase.GetCategoryUseCase
import com.example.harmoney.domain.category.api.useCase.UpdateCategoryUseCase
import com.example.harmoney.domain.category.models.CategoryFailure
import com.example.harmoney.domain.models.Category
import com.example.harmoney.domain.models.CategoryColors
import com.example.harmoney.domain.models.CategoryIcon
import com.example.harmoney.domain.models.CategoryIcons
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.presentation.category.models.CategoryAction
import com.example.harmoney.presentation.category.models.CategoryEvent
import com.example.harmoney.presentation.category.models.CategoryNameError
import com.example.harmoney.presentation.category.models.CategoryState
import com.example.harmoney.presentation.category.models.EditableCategory
import com.example.harmoney.presentation.converters.DateFormatter
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.LocalDateTime

@Suppress("detekt:TooManyFunctions", "detekt:LongParameterList")
class CategoryViewModel(
    private val sessionStateHolder: SessionStateHolder,
    private val categoryId: Long?,
    private val getCategoryUseCase: GetCategoryUseCase,
    private val addCategoryUseCase: AddCategoryUseCase,
    private val updateCategoryUseCase: UpdateCategoryUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase,
    private val checkCategoryAlreadyExistsUseCase: CheckCategoryAlreadyExistsUseCase
) : BaseViewModel<CategoryEvent, CategoryAction, CategoryState>(
    CategoryState(
        selectedCategoryType = sessionStateHolder.state.value.categoryType,
        isCreateCategoryScreen = categoryId == null
    )
) {
    override val tag: String = CategoryViewModel::class.java.simpleName ?: ""

    private var initialCategory: Category = createEmptyCategory()
    private var editableCategory: EditableCategory = EditableCategory(
        type = initialCategory.type,
        name = initialCategory.name,
        icon = initialCategory.icon.icon,
        iconColor = initialCategory.icon.color
    )

    init {
        launchWithErrorHandling(
            errorMessage = GET_CATEGORY_UNEXPECTED_ERROR,
            onError = { writableAction.emit(CategoryAction.DataLoadingError) }
        ) {
            val category = if (categoryId == null) {
                createEmptyCategory()
            } else {
                when (val result = getCategoryUseCase.execute(categoryId)) {
                    is Resource.Success -> result.data
                    is Resource.Error -> {
                        writableState.update { it.copy(isDataLoadingErrorDialogOpened = true) }
                        createEmptyCategory()
                    }
                }
            }

            applyInitialCategory(category)
        }

        sessionStateHolder.state
            .map { it.categoryType }
            .distinctUntilChanged()
            .onEach { categoryType ->
                editableCategory = editableCategory.copy(type = categoryType)

                writableState.update {
                    it.copy(selectedCategoryType = categoryType)
                }
            }.launchIn(viewModelScope)
    }

    private fun createEmptyCategory(): Category {
        return Category(
            id = ZERO_ID,
            name = EMPTY_STRING,
            type = state.value.selectedCategoryType,
            icon = CategoryIcon(
                icon = state.value.selectedIcon,
                color = state.value.selectedColor
            )
        )
    }

    private fun applyInitialCategory(category: Category) {
        initialCategory = category

        editableCategory = EditableCategory(
            type = initialCategory.type,
            name = initialCategory.name,
            icon = initialCategory.icon.icon,
            iconColor = initialCategory.icon.color
        )
        sessionStateHolder.setCategoryType(editableCategory.type)

        writableState.update {
            it.copy(
                selectedCategoryType = editableCategory.type,
                categoryName = editableCategory.name,
                selectedIcon = editableCategory.icon,
                selectedColor = editableCategory.iconColor,
                isDataReadyForEditing = true,
                initCategoryName = editableCategory.name
            )
        }
    }

    @Suppress("detekt:CyclomaticComplexMethod")
    override fun obtainEvent(event: CategoryEvent) {
        when (event) {
            is CategoryEvent.OnBackClick -> onBackClick()
            is CategoryEvent.OnBackDialogConfirm -> onBackDialogConfirm()
            is CategoryEvent.OnBackDialogDismiss -> onBackDialogDismiss()

            is CategoryEvent.OnChangeCategoryTypeClick -> {
                onChangedCategoryTypeClick(event.newCategoryType)
            }

            is CategoryEvent.OnCategoryNameChanged -> onCategoryNameChanged(event.newName)

            is CategoryEvent.OnOpenIconsBottomSheetClick -> onOpenIconsBottomSheetClick()
            is CategoryEvent.OnIconsBottomSheetDismiss -> onIconsBottomSheetDismiss()
            is CategoryEvent.OnIconClick -> onIconClick(event.newIcon)

            is CategoryEvent.OnColorClick -> onColorClick(event.newColor)

            is CategoryEvent.OnSaveClick -> onSaveClick()
            is CategoryEvent.OnSaveDialogDismiss -> onSaveDialogDismiss()

            is CategoryEvent.OnDeleteClick -> onDeleteClick()
            is CategoryEvent.OnDeleteDialogConfirm -> onDeleteDialogConfirm()
            is CategoryEvent.OnDeleteDialogDismiss -> onDeleteDialogDismiss()

            is CategoryEvent.OnDataLoadingErrorDialogConfirm -> onDataLoadingErrorDialogConfirm()
        }
    }

    private fun onBackClick() {
        val isEdited = editableCategory.type.id != initialCategory.type.id
                || editableCategory.name != initialCategory.name
                || editableCategory.icon.id != initialCategory.icon.icon.id
                || editableCategory.iconColor.id != initialCategory.icon.color.id

        if (isEdited) {
            writableState.update { it.copy(isCategoryNotSavedDialogOpened = true) }
        } else {
            onNavigateBack()
        }
    }

    private fun onBackDialogConfirm() {
        writableState.update { it.copy(isCategoryNotSavedDialogOpened = false) }
        onNavigateBack()
    }

    private fun onBackDialogDismiss() {
        writableState.update { it.copy(isCategoryNotSavedDialogOpened = false) }
    }

    private fun onNavigateBack() {
        writableAction.tryEmit(CategoryAction.NavigateBack)
    }

    private fun onChangedCategoryTypeClick(newCategoryType: CategoryType) {
        if (editableCategory.type.id != newCategoryType.id) {

            sessionStateHolder.setCategoryType(newCategoryType)

            if (state.value.categoryNameError is CategoryNameError.AlreadyExists) {
                writableState.update { it.copy(categoryNameError = CategoryNameError.None) }
            }
        }
    }

    private fun onCategoryNameChanged(newName: String) {
        if (newName != editableCategory.name) {

            editableCategory = editableCategory.copy(name = newName)
            writableState.update {
                it.copy(
                    categoryName = editableCategory.name,
                    categoryNameError = CategoryNameError.None
                )
            }
        }
    }

    private fun onOpenIconsBottomSheetClick() {
        writableState.update { it.copy(isIconsBottomSheetOpened = true) }
    }

    private fun onIconsBottomSheetDismiss() {
        writableState.update { it.copy(isIconsBottomSheetOpened = false) }
    }

    private fun onIconClick(newIcon: CategoryIcons) {
        if (editableCategory.icon.id != newIcon.id) {
            editableCategory = editableCategory.copy(icon = newIcon)

            writableState.update {
                it.copy(selectedIcon = newIcon, isIconsBottomSheetOpened = false)
            }
        } else {
            onIconsBottomSheetDismiss()
        }
    }

    private fun onColorClick(newColor: CategoryColors) {
        if (editableCategory.iconColor.id != newColor.id) {
            editableCategory = editableCategory.copy(iconColor = newColor)

            writableState.update { it.copy(selectedColor = newColor) }
        }
    }

    private fun onSaveClick() {
        launchWithErrorHandling(
            errorMessage = SAVE_CATEGORY_UNEXPECTED_ERROR,
            onError = { writableAction.emit(CategoryAction.SaveCategoryError) }
        ) {
            val categoryNameError = checkCategoryName(
                name = editableCategory.name,
                categoryType = editableCategory.type,
                isCreateCategoryScreen = state.value.isCreateCategoryScreen
            )

            if (categoryNameError !is CategoryNameError.None) {
                writableState.update {
                    it.copy(
                        isSaveCategoryErrorDialogOpened = true,
                        categoryNameError = categoryNameError
                    )
                }
                return@launchWithErrorHandling
            }

            val isEdited = editableCategory.name != initialCategory.name
                    || editableCategory.type.id != initialCategory.type.id
                    || editableCategory.icon.id != initialCategory.icon.icon.id
                    || editableCategory.iconColor.id != initialCategory.icon.color.id

            if (!isEdited) {
                onNavigateBack()
                return@launchWithErrorHandling
            }

            if (state.value.isCreateCategoryScreen) {
                createCategory()
            } else {
                updateCategory()
            }
        }
    }

    private suspend fun createCategory() {
        val result = addCategoryUseCase.execute(
            category = Category(
                name = editableCategory.name,
                type = editableCategory.type,
                icon = CategoryIcon(
                    icon = editableCategory.icon,
                    color = editableCategory.iconColor
                ),
                createdAt = System.currentTimeMillis()
            )
        )

        when (result) {
            is Resource.Success -> {
                writableAction.emit(
                    CategoryAction.CreatedSuccessfully(editableCategory.name)
                )
            }

            is Resource.Error -> {
                writableAction.emit(CategoryAction.CreateCategoryError)
            }
        }
    }

    private suspend fun updateCategory() {
        val result = updateCategoryUseCase.execute(
            initialCategory.copy(
                name = editableCategory.name,
                type = editableCategory.type,
                icon = CategoryIcon(
                    icon = editableCategory.icon,
                    color = editableCategory.iconColor
                )
            )
        )

        when (result) {
            is Resource.Success -> {
                writableAction.emit(CategoryAction.UpdatedSuccessfully)
            }

            is Resource.Error -> {
                writableAction.emit(CategoryAction.UpdateCategoryError)
            }
        }
    }

    private fun onSaveDialogDismiss() {
        writableState.update { it.copy(isSaveCategoryErrorDialogOpened = false) }
    }

    private fun onDeleteClick() {
        writableState.update { it.copy(isCategoryDeleteDialogOpened = true) }
    }

    private fun onDeleteDialogConfirm() {
        launchWithErrorHandling(
            errorMessage = DELETE_CATEGORY_UNEXPECTED_ERROR,
            onError = { writableAction.emit(CategoryAction.DeleteCategoryError) }
        ) {
            val result = deleteCategoryUseCase.execute(
                initialCategory
            )

            when (result) {
                is Resource.Success -> {
                    writableState.update { it.copy(isCategoryDeleteDialogOpened = false) }
                    writableAction.emit(
                        CategoryAction.DeletedSuccessfully(editableCategory.name)
                    )
                }

                is Resource.Error -> {
                    writableState.update { it.copy(isCategoryDeleteDialogOpened = false) }
                    writableAction.emit(CategoryAction.DeleteCategoryError)
                }
            }
        }
    }

    private fun onDeleteDialogDismiss() {
        writableState.update { it.copy(isCategoryDeleteDialogOpened = false) }
    }

    private suspend fun checkCategoryName(
        name: String,
        categoryType: CategoryType,
        isCreateCategoryScreen: Boolean
    ): CategoryNameError {

        return if (name.isEmpty()) {
            CategoryNameError.Empty
        } else if (!isCreateCategoryScreen && name == initialCategory.name) {
            CategoryNameError.None
        } else {
            val result = checkCategoryAlreadyExistsUseCase.execute(
                categoryName = name,
                categoryType = categoryType
            )

            when (result) {
                is Resource.Success -> {
                    if (result.data) {
                        CategoryNameError.AlreadyExists
                    } else {
                        CategoryNameError.None
                    }
                }

                is Resource.Error -> {
                    when (result.error) {
                        CategoryFailure.BadRequest -> CategoryNameError.None
                        CategoryFailure.DatabaseError -> {
                            writableAction.emit(
                                CategoryAction.CheckingCategoryAlreadyExistsError
                            )
                            CategoryNameError.CheckFailed
                        }
                    }
                }
            }
        }
    }

    private fun onDataLoadingErrorDialogConfirm() {
        writableState.update { it.copy(isDataLoadingErrorDialogOpened = false) }
        onNavigateBack()
    }

    private companion object {
        const val ZERO_ID = 0L
        const val EMPTY_STRING = ""
        const val GET_CATEGORY_UNEXPECTED_ERROR = "Error receiving the category"
        const val SAVE_CATEGORY_UNEXPECTED_ERROR =
            "Error saving (creating or updating) category"
        const val DELETE_CATEGORY_UNEXPECTED_ERROR = "Error deleting category"
    }
}
