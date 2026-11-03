package com.example.harmoney.presentation.currency.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.harmoney.core.util.BaseViewModel
import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.currencyList.api.useCase.GetSupportedCurrencyListUseCase
import com.example.harmoney.domain.currencyList.models.CurrencyListFailure
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.navigation.NavResultKeys
import com.example.harmoney.presentation.converters.CurrencyUiConverter
import com.example.harmoney.presentation.currency.models.CurrencyAction
import com.example.harmoney.presentation.currency.models.CurrencyEvent
import com.example.harmoney.presentation.currency.models.CurrencyScreenError
import com.example.harmoney.presentation.currency.models.CurrencyState
import com.example.harmoney.presentation.models.ContentState
import com.example.harmoney.presentation.models.CurrencyUi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CurrencyViewModel(
    selectedCurrencyId: Long,
    private val savedStateHandle: SavedStateHandle,
    private val currencyUiConverter: CurrencyUiConverter,
    private val getSupportedCurrencyListUseCase: GetSupportedCurrencyListUseCase,
) : BaseViewModel<CurrencyEvent, CurrencyAction, CurrencyState>(
    CurrencyState(selectedCurrencyId = selectedCurrencyId)
) {
    private var initCurrencyList: List<CurrencyUi> =
        currencyUiConverter.map(Currency.entries).sortedBy { it.name }

    private val typedQuery = MutableStateFlow("")

    init {
        getSupportedCurrencyList()
        observeSearchQuery()
    }

    private fun getSupportedCurrencyList(isRefreshRequired: Boolean = false) {
        writableState.update { it.copy(content = ContentState.Loading) }

        launchWithErrorHandling(
            errorMessage = GET_SUPPORTED_CURRENCY_LIST_UNEXPECTED_ERROR,
            onError = {
                writableState.update {
                    it.copy(
                        content = ContentState.Error(
                            CurrencyScreenError.UPDATE_DATA_ERROR
                        )
                    )
                }
            }
        ) {
            var contentState: ContentState<List<CurrencyUi>, CurrencyScreenError> =
                ContentState.Idle
            when (val res = getSupportedCurrencyListUseCase.execute(isRefreshRequired)) {
                is Resource.Success -> {
                    initCurrencyList = currencyUiConverter.map(res.data)
                    contentState = ContentState.Success(initCurrencyList)
                }

                is Resource.Error -> {
                    initCurrencyList = emptyList()

                    contentState = when (res.error) {
                        is CurrencyListFailure.NetworkConnectionError -> {
                            ContentState.Error(
                                CurrencyScreenError
                                    .NETWORK_CONNECTION_ERROR
                            )
                        }

                        else -> {
                            ContentState.Error(CurrencyScreenError.UPDATE_DATA_ERROR)
                        }
                    }
                }
            }

            writableState.update {
                it.copy(
                    content = contentState,
                    refreshCurrencyListId = state.value.refreshCurrencyListId + 1
                )
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQuery() {
        viewModelScope.launch {
            typedQuery
                .debounce { SEARCH_DEBOUNCE }
                .distinctUntilChanged()
                .collectLatest { query ->
                    searchCurrency(query)
                }
        }
    }

    override fun obtainEvent(event: CurrencyEvent) {
        when (event) {
            is CurrencyEvent.OnBackClick -> onBackClick()
            is CurrencyEvent.OnSearchTextChanged -> onSearchTextChanged(event.newText)
            is CurrencyEvent.OnClearSearchLineClick -> onClearSearchLineClick()
            is CurrencyEvent.OnSearchStartClick -> searchCurrency(state.value.searchText)
            is CurrencyEvent.OnCurrencyClick -> onCurrencyClick(event.newCurrency)
            is CurrencyEvent.OnRefreshClick -> onRefreshClick()
        }
    }

    private fun onBackClick() {
        onReturnWithoutParam()
    }

    private fun onReturnWithoutParam() {
        savedStateHandle[NavResultKeys.SELECTED_CURRENCY] = null
        writableAction.tryEmit(CurrencyAction.NavigateBack)
    }

    private fun onReturnWithParam(param: Long) {
        savedStateHandle[NavResultKeys.SELECTED_CURRENCY] = param
        writableAction.tryEmit(CurrencyAction.NavigateBack)
    }

    private fun onSearchTextChanged(newText: String) {
        typedQuery.value = newText
        writableState.update { it.copy(searchText = newText) }
    }

    private fun onClearSearchLineClick() {
        typedQuery.value = ""
        val content = when (writableState.value.content) {
            is ContentState.Success -> ContentState.Success(initCurrencyList)
            else -> writableState.value.content
        }
        writableState.update {
            it.copy(
                searchText = "",
                content = content,
                refreshCurrencyListId = state.value.refreshCurrencyListId + 1
            )
        }
    }

    private fun onCurrencyClick(newCurrency: CurrencyUi) {
        if (state.value.selectedCurrencyId != newCurrency.currency.id) {
            onReturnWithParam(newCurrency.currency.id)
        }
    }

    private fun searchCurrency(query: String) {
        val result = if (query.isBlank()) {
            initCurrencyList
        } else {
            initCurrencyList.filter { item ->
                item.name.contains(query, ignoreCase = true) ||
                        item.currency.isoCode.contains(query, ignoreCase = true)
            }
        }

        writableState.update {
            it.copy(
                content = ContentState.Success(result),
                refreshCurrencyListId = state.value.refreshCurrencyListId + 1
            )
        }
    }

    private fun onRefreshClick() {
        getSupportedCurrencyList(isRefreshRequired = true)
    }

    companion object {
        private const val SEARCH_DEBOUNCE = 1000L
        private const val GET_SUPPORTED_CURRENCY_LIST_UNEXPECTED_ERROR =
            "Error_getting_supported_currency_list"
    }
}
