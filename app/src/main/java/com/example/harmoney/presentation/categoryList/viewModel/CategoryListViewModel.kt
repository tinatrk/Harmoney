package com.example.harmoney.presentation.categoryList.viewModel

import androidx.lifecycle.viewModelScope
import com.example.harmoney.base.BaseViewModel
import com.example.harmoney.core.session.SessionStateHolder
import com.example.harmoney.domain.models.SortOption
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.presentation.categoryList.models.CategoryListAction
import com.example.harmoney.presentation.categoryList.models.CategoryListEvent
import com.example.harmoney.presentation.categoryList.models.CategoryListState
import com.example.harmoney.presentation.categoryStatistics.models.CategoryStatisticsState
import com.example.harmoney.presentation.converters.CategoryUiConverter
import com.example.harmoney.presentation.test.TestDataSource
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CategoryListViewModel(
    private val sessionSateHolder: SessionStateHolder,
    private val test: TestDataSource,
    private val categoryUiConverter: CategoryUiConverter
) : BaseViewModel<CategoryListEvent, CategoryListAction, CategoryListState>(
    state = CategoryListState(
        selectedCategoryType = sessionSateHolder.state.value.categoryType,
        selectedTabIndex = sessionSateHolder.state.value.categoryType.ordinal
    )
) {
    override val tag: String = CategoryListViewModel::class.java.simpleName ?: ""

    init {
        // TODO() считать тип сортировки
        sessionSateHolder.state.map { it.categoryType }
            .distinctUntilChanged()
            .flatMapLatest { categoryType ->
                flow {
                    val sortOption = test.getSortOption()
                    val categories = test.getCategories(categoryType) //sortOption
                    emit(Triple(categoryType, sortOption, categories))
                }
        }.onEach { (categoryType, sortOption, categories) ->
                writableState.update {
                    it.copy(
                        selectedSortOption = sortOption,
                        selectedCategoryType = categoryType,
                        selectedTabIndex = categoryType.ordinal,
                        categories = categoryUiConverter.map(categories)
                    )
                }
            }.launchIn(viewModelScope)
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

            sessionSateHolder.setCategoryType(newCategoryType)
        }
    }

    private fun onSortMenuClick() {
        writableState.update { it.copy(isSortMenuOpened = true) }
    }

    private fun onSortMenuDismiss() {
        writableState.update { it.copy(isSortMenuOpened = false) }
    }

    private fun onSortOptionClick(newSortOption: SortOption) {
        if (state.value.selectedSortOption != newSortOption) {
            // в будущем обновлять sortOption через UseCase, а категории обновятся автоматически
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
        // пока при смене порядка категории автоматически не обновятся
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
