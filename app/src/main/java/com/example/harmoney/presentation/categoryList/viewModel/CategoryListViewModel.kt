package com.example.harmoney.presentation.categoryList.viewModel

import com.example.harmoney.base.BaseViewModel
import com.example.harmoney.domain.models.CategorySortOption
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.presentation.categoryList.models.CategoryListAction
import com.example.harmoney.presentation.categoryList.models.CategoryListEvent
import com.example.harmoney.presentation.categoryList.models.CategoryListState
import com.example.harmoney.presentation.converters.CategoryUiConverter
import com.example.harmoney.presentation.test.TestDataSource
import kotlinx.coroutines.flow.update

class CategoryListViewModel(
    categoryType: CategoryType,
    private val test: TestDataSource,
    private val categoryUiConverter: CategoryUiConverter
) : BaseViewModel<CategoryListEvent, CategoryListAction, CategoryListState>(
    state = CategoryListState(
        selectedCategoryType = categoryType,
        selectedTabIndex = categoryType.ordinal,
    )
) {
    override val tag: String = CategoryListViewModel::class.java.simpleName ?: ""

    init {
        // TODO() считать тип сортировки
        val sortOption = test.getSortOption()

        val categories = test.getCategories(categoryType)

        writableState.update {
            it.copy(
                selectedSortOption = sortOption,
                categories = categoryUiConverter.map(categories)
            )
        }
    }

    override fun obtainEvent(event: CategoryListEvent) {
        when (event) {
            is CategoryListEvent.OnBackClick -> onNavigateBack()

            is CategoryListEvent.OnSortMenuClick -> onSortMenuClick()
            is CategoryListEvent.OnSortMenuDismiss -> onSortMenuDismiss()
            is CategoryListEvent.OnSortOptionClick -> onSortOptionClick(event.newSortOption)

            is CategoryListEvent.OnCategoryUserOrderChanged -> {
                onUpdateCategoryUserOrder(event.from, event.to)
            }

            is CategoryListEvent.OnTabClick -> onTabClick(event.newCategoryType)

            is CategoryListEvent.OnCategoryClick -> onOpenCategory(event.categoryId)

            is CategoryListEvent.OnFloatingButtonClick -> onCreateCategory()
        }
    }

    private fun onTabClick(newCategoryType: CategoryType) {
        if (state.value.selectedCategoryType.id != newCategoryType.id) {
            val categories = test.getCategories(newCategoryType)

            writableState.update {
                it.copy(
                    selectedCategoryType = newCategoryType,
                    selectedTabIndex = newCategoryType.ordinal,
                    categories = categoryUiConverter.map(categories)
                )
            }
        }
    }

    private fun onSortMenuClick() {
        writableState.update { it.copy(isSortMenuOpened = true) }
    }

    private fun onSortMenuDismiss() {
        writableState.update { it.copy(isSortMenuOpened = false) }
    }

    private fun onSortOptionClick(newSortOption: CategorySortOption) {
        if (state.value.selectedSortOption != newSortOption) {
            test.updateCategorySortOption(newSortOption)
            val categories = test.getCategories(state.value.selectedCategoryType)

            writableState.update {
                it.copy(
                    selectedSortOption = newSortOption,
                    categories = categoryUiConverter.map(categories),
                    isSortMenuOpened = false
                )
            }
        } else {
            onSortMenuDismiss()
        }
    }

    private fun onUpdateCategoryUserOrder(from: Int, to: Int) {
        if (from == to) return
        test.updateCategoryUserOrder(from, to, state.value.selectedCategoryType)
        writableState.update {
            it.copy(
                categories = categoryUiConverter.map(
                    test.getCategories(state.value.selectedCategoryType)
                )
            )
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
