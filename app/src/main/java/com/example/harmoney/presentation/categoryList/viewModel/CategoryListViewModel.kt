package com.example.harmoney.presentation.categoryList.viewModel

import com.example.harmoney.base.BaseViewModel
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.presentation.categoryList.models.CategoryListAction
import com.example.harmoney.presentation.categoryList.models.CategoryListEvent
import com.example.harmoney.presentation.categoryList.models.CategoryListState
import kotlinx.coroutines.flow.update

class CategoryListViewModel(
    categoryType: CategoryType,
) : BaseViewModel<CategoryListEvent, CategoryListAction, CategoryListState>(
    state = CategoryListState()
) {
    override val tag: String = CategoryListViewModel::class.java.simpleName ?: ""

    init {
        writableState.update {
            it.copy(
                selectedCategoryType = categoryType,
                selectedTabIndex = categoryType.ordinal,
            )
        }
    }

    override fun obtainEvent(event: CategoryListEvent) {
        when (event) {
            is CategoryListEvent.OnBackClick -> onNavigateBack()

            is CategoryListEvent.OnSortMenuClick -> {}
            is CategoryListEvent.OnSortMenuDismiss -> {}
            is CategoryListEvent.OnSortOptionClick -> {}

            is CategoryListEvent.OnTabClick -> onTabClick(event.newCategoryType)

            is CategoryListEvent.OnCategoryClick -> onOpenCategory(event.categoryId)

            is CategoryListEvent.OnFloatingButtonClick -> onCreateCategory()
        }
    }

    private fun onTabClick(newCategoryType: CategoryType) {
        if (state.value.selectedCategoryType.id != newCategoryType.id) {
            writableState.update {
                it.copy(
                    selectedCategoryType = newCategoryType,
                    selectedTabIndex = newCategoryType.ordinal
                )
            }
        }
    }

    private fun onCreateCategory() {
        writableAction.tryEmit(CategoryListAction.NavigateToCreatingCategory)
    }

    private fun onOpenCategory(categoryId: Long?) {
        writableAction.tryEmit(CategoryListAction.NavigateToOpeningCategory(categoryId))
    }

    private fun onNavigateBack() {
        writableAction.tryEmit(CategoryListAction.NavigateBack)
    }
}
