package com.example.harmoney.presentation.category.viewModel

import com.example.harmoney.base.BaseViewModel
import com.example.harmoney.domain.models.Category
import com.example.harmoney.domain.models.CategoryIcon
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.presentation.category.models.CategoryAction
import com.example.harmoney.presentation.category.models.CategoryEvent
import com.example.harmoney.presentation.category.models.CategoryNameError
import com.example.harmoney.presentation.category.models.CategoryState
import com.example.harmoney.presentation.converters.CategoryUiConverter
import com.example.harmoney.presentation.models.CategoryUi
import com.example.harmoney.presentation.test.TestDataSource
import kotlinx.coroutines.flow.update

class CategoryViewModel(
    categoryType: CategoryType,
    categoryId: Long?,
    private val test: TestDataSource,
    private val categoryUiConverter: CategoryUiConverter
) : BaseViewModel<CategoryEvent, CategoryAction, CategoryState>(
    CategoryState(
        selectedCategoryType = categoryType,
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

    @Suppress(
        "detekt:CyclomaticComplexMethod"
    )
    override fun obtainEvent(event: CategoryEvent) {
        when (event) {
            is CategoryEvent.OnBackClick -> onBackClick()
            is CategoryEvent.OnBackDialogConfirm -> onBackDialogConfirm()
            is CategoryEvent.OnBackDialogDismiss -> onBackDialogDismiss()

            is CategoryEvent.OnChangeCategoryTypeClick -> {}

            is CategoryEvent.OnCategoryNameChanged -> {}

            is CategoryEvent.OnOpenIconsBottomSheetClick -> {}
            is CategoryEvent.OnIconsBottomSheetDismiss -> {}
            is CategoryEvent.OnIconClick -> {}

            is CategoryEvent.OnColorClick -> {}

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

    private fun onSaveClick() {
        val categoryNameError = when {
            curCategory.name.isEmpty() -> CategoryNameError.Empty
            state.value.isCreateCategoryScreen && test.isCategoryAlreadyExists(
                categoryName = curCategory.name, categoryType = curCategory.type
            ) -> CategoryNameError.AlreadyExists

            else -> CategoryNameError.None
        }

        if (categoryNameError !is CategoryNameError.None) {
            writableState.update {
                it.copy(
                    isSaveCategoryErrorDialogOpened = true,
                    categoryNameError = categoryNameError
                )
            }
        } else {
            if (categoryUiConverter.map(curCategory) != initialCategoryUi) {
                test.saveCategory(curCategory)
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

    private companion object {
        const val ZERO_ID = 0L
        const val EMPTY_STRING = ""
    }
}
