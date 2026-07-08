package com.example.harmoney.presentation.category.viewModel

import com.example.harmoney.base.BaseViewModel
import com.example.harmoney.core.session.SessionStateHolder
import com.example.harmoney.domain.models.Category
import com.example.harmoney.domain.models.CategoryColors
import com.example.harmoney.domain.models.CategoryIcon
import com.example.harmoney.domain.models.CategoryIcons
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.presentation.category.models.CategoryAction
import com.example.harmoney.presentation.category.models.CategoryEvent
import com.example.harmoney.presentation.category.models.CategoryNameError
import com.example.harmoney.presentation.category.models.CategoryState
import com.example.harmoney.presentation.converters.CategoryUiConverter
import com.example.harmoney.presentation.models.CategoryUi
import com.example.harmoney.presentation.test.TestDataSource
import kotlinx.coroutines.flow.update

@Suppress("detekt:TooManyFunctions")
class CategoryViewModel(
    private val sessionSateHolder: SessionStateHolder,
    categoryId: Long?,
    private val test: TestDataSource,
    private val categoryUiConverter: CategoryUiConverter
) : BaseViewModel<CategoryEvent, CategoryAction, CategoryState>(
    CategoryState(
        selectedCategoryType = sessionSateHolder.state.value.categoryType,
        isCreateCategoryScreen = categoryId == null
    )
) {
    override val tag: String = CategoryViewModel::class.java.simpleName ?: ""

    val initialCategoryUi: CategoryUi
    var curCategory: Category

    init {
        curCategory = getInitialCategory(categoryId)
        initialCategoryUi = categoryUiConverter.map(curCategory)

        writableState.update {
            it.copy(
                selectedCategoryType = initialCategoryUi.type,
                categoryName = initialCategoryUi.name,
                selectedIcon = initialCategoryUi.icon.icon,
                selectedColor = initialCategoryUi.icon.color
            )
        }
    }

    private fun getInitialCategory(categoryId: Long?): Category {
        val emptyCategory = Category(
            id = ZERO_ID,
            name = EMPTY_STRING,
            type = state.value.selectedCategoryType,
            icon = CategoryIcon(
                icon = state.value.selectedIcon,
                color = state.value.selectedColor
            )
        )

        return if (state.value.isCreateCategoryScreen) {
            emptyCategory
        } else {
            test.getCategory(categoryId) ?: emptyCategory
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
        }
    }

    private fun onBackClick() {
        val curCategoryUi = categoryUiConverter.map(curCategory)

        if (initialCategoryUi != curCategoryUi) {
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
        if (curCategory.type != newCategoryType) {
            curCategory = curCategory.copy(type = newCategoryType)

            sessionSateHolder.setCategoryType(newCategoryType)

            val categoryNameError = checkCategoryName(
                name = curCategory.name,
                categoryType = curCategory.type,
                isCreateCategoryScreen = state.value.isCreateCategoryScreen
            )

            writableState.update {
                it.copy(
                    selectedCategoryType = newCategoryType,
                    categoryNameError = categoryNameError
                )
            }
        }
    }

    private fun onCategoryNameChanged(newName: String) {
        if (newName != curCategory.name) {
            curCategory = curCategory.copy(name = newName)

            val categoryNameError = checkCategoryName(
                name = curCategory.name,
                categoryType = curCategory.type,
                isCreateCategoryScreen = state.value.isCreateCategoryScreen
            )

            writableState.update {
                it.copy(
                    categoryName = curCategory.name,
                    categoryNameError = categoryNameError
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
        if (curCategory.icon.icon != newIcon) {
            curCategory = curCategory.copy(icon = curCategory.icon.copy(icon = newIcon))

            writableState.update {
                it.copy(selectedIcon = newIcon, isIconsBottomSheetOpened = false)
            }
        } else {
            onIconsBottomSheetDismiss()
        }
    }

    private fun onColorClick(newColor: CategoryColors) {
        if (curCategory.icon.color != newColor) {
            curCategory = curCategory.copy(icon = curCategory.icon.copy(color = newColor))

            writableState.update { it.copy(selectedColor = newColor) }
        }
    }

    private fun onSaveClick() {
        val categoryNameError = checkCategoryName(
            name = curCategory.name,
            categoryType = curCategory.type,
            isCreateCategoryScreen = state.value.isCreateCategoryScreen
        )

        if (categoryNameError !is CategoryNameError.None) {
            writableState.update {
                it.copy(
                    isSaveCategoryErrorDialogOpened = true,
                    categoryNameError = categoryNameError
                )
            }
        } else {
            if (categoryUiConverter.map(curCategory) != initialCategoryUi) {
                if (state.value.isCreateCategoryScreen) {
                    test.createCategory(curCategory)
                } else {
                    test.updateCategory(curCategory)
                }
            }
            onNavigateBack()
        }
    }

    private fun onSaveDialogDismiss() {
        writableState.update { it.copy(isSaveCategoryErrorDialogOpened = false) }
    }

    private fun onDeleteClick() {
        writableState.update { it.copy(isCategoryDeleteDialogOpened = true) }
    }

    private fun onDeleteDialogConfirm() {
        test.deleteCategory(curCategory)
        writableState.update { it.copy(isCategoryDeleteDialogOpened = false) }
        onNavigateBack()
    }

    private fun onDeleteDialogDismiss() {
        writableState.update { it.copy(isCategoryDeleteDialogOpened = false) }
    }

    private fun checkCategoryName(
        name: String,
        categoryType: CategoryType,
        isCreateCategoryScreen: Boolean
    ): CategoryNameError {
        return when {
            name.isEmpty() -> CategoryNameError.Empty
            isCreateCategoryScreen && test.isCategoryAlreadyExists(
                categoryName = name, categoryType = categoryType
            ) -> CategoryNameError.AlreadyExists

            else -> CategoryNameError.None
        }
    }

    private companion object {
        const val ZERO_ID = 0L
        const val EMPTY_STRING = ""
    }
}
