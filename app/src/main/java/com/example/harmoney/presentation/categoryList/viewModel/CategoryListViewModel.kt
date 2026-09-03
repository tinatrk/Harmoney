package com.example.harmoney.presentation.categoryList.viewModel

import com.example.harmoney.base.BaseViewModel
import com.example.harmoney.core.session.SessionStateHolder
import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.category.api.useCase.GetCategoryListUseCase
import com.example.harmoney.domain.category.api.useCase.UpdateCategoryUserOrderUseCase
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.SortOption
import com.example.harmoney.domain.settings.categorySortingMode.api.useCase.CategorySortOptionInteractor
import com.example.harmoney.presentation.categoryList.models.CategoryListAction
import com.example.harmoney.presentation.categoryList.models.CategoryListEvent
import com.example.harmoney.presentation.categoryList.models.CategoryListState
import com.example.harmoney.presentation.converters.CategoryUiConverter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

@OptIn(ExperimentalCoroutinesApi::class)
class CategoryListViewModel(
    private val sessionSateHolder: SessionStateHolder,
    private val categoryUiConverter: CategoryUiConverter,
    private val categorySortOptionInteractor: CategorySortOptionInteractor,
    private val getCategoryListUseCase: GetCategoryListUseCase,
    private val updateCategoryUserOrderUseCase: UpdateCategoryUserOrderUseCase
) : BaseViewModel<CategoryListEvent, CategoryListAction, CategoryListState>(
    state = CategoryListState(
        selectedCategoryType = sessionSateHolder.state.value.categoryType,
        selectedTabIndex = sessionSateHolder.state.value.categoryType.ordinal
    )
) {
    override val tag: String = CategoryListViewModel::class.java.simpleName ?: ""

    init {
        combine(
            sessionSateHolder.state.map { it.categoryType }.distinctUntilChanged(),
            categorySortOptionInteractor.getSortOption().distinctUntilChanged()
        ) { categoryType, sortOption ->
            categoryType to sortOption
        }.flatMapLatest { (categoryType, sortOption) ->

            getCategoryListUseCase.execute(categoryType, sortOption)
                .map { getCategoryListResult ->
                    Triple(
                        categoryType,
                        sortOption,
                        getCategoryListResult
                    )
                }
        }.onEach { (categoryType, sortOption, getCategoryListResult) ->
            when (getCategoryListResult) {
                is Resource.Success -> {
                    writableState.update {
                        it.copy(
                            selectedSortOption = sortOption,
                            selectedCategoryType = categoryType,
                            selectedTabIndex = categoryType.ordinal,
                            categories = categoryUiConverter
                                .map(getCategoryListResult.data),
                            isDataLoadingError = false
                        )
                    }
                }

                is Resource.Error -> {
                    writableAction.emit(CategoryListAction.GetCategoryListError)
                    writableState.update { it.copy(isDataLoadingError = true) }
                }
            }
        }.collectWithErrorHandling(
            errorMessage = GET_CATEGORY_LIST_UNEXPECTED_ERROR,
            onError = {
                writableAction.emit(CategoryListAction.GetCategoryListError)
                writableState.update { it.copy(isDataLoadingError = true) }
            }
        )
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

            launchWithErrorHandling(
                errorMessage = SET_SORT_OPTION_UNEXPECTED_ERROR,
                onError = { writableAction.emit(CategoryListAction.SetSortOptionError) }
            ) {
                categorySortOptionInteractor.setSortOption(newSortOption)

                writableState.update {
                    it.copy(
                        isSortMenuOpened = false
                    )
                }
            }
        } else {
            onSortMenuDismiss()
        }
    }

    private fun onUpdateCategoryUserOrder(from: Int, to: Int) {
        if (from == to) return

        launchWithErrorHandling(
            errorMessage = UPDATE_CATEGORY_USER_ORDER_ERROR,
            onError = {
                writableAction.emit(CategoryListAction.UpdateCategoryUserOrderError)
            }
        ) {
            val result = updateCategoryUserOrderUseCase.execute(
                from,
                to,
                categoryUiConverter.map(state.value.categories)
            )

            when (result) {
                is Resource.Success -> Unit

                is Resource.Error -> {
                    writableAction.emit(CategoryListAction.UpdateCategoryUserOrderError)
                }
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

    companion object {
        private const val GET_CATEGORY_LIST_UNEXPECTED_ERROR = "Error_getting_category_list"
        private const val SET_SORT_OPTION_UNEXPECTED_ERROR = "Error_setting_sort_option"
        private const val UPDATE_CATEGORY_USER_ORDER_ERROR = "Error_updating_category_user_order"
    }
}
