package com.example.harmoney.presentation.currency.viewModel

import androidx.lifecycle.viewModelScope
import com.example.harmoney.core.util.BaseViewModel
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.settings.currency.api.useCase.SetReportingCurrencyUseCase
import com.example.harmoney.presentation.converters.CurrencyUiConverter
import com.example.harmoney.presentation.currency.models.CurrencyAction
import com.example.harmoney.presentation.currency.models.CurrencyEvent
import com.example.harmoney.presentation.currency.models.CurrencyState
import com.example.harmoney.presentation.models.CurrencyUi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CurrencyViewModel(
    selectedCurrencyId: Long,
    private val setReportingCurrencyUseCase: SetReportingCurrencyUseCase,
    private val currencyUiConverter: CurrencyUiConverter,
) : BaseViewModel<CurrencyEvent, CurrencyAction, CurrencyState>(
    CurrencyState(selectedCurrencyId = selectedCurrencyId)
) {
    override val tag: String = CurrencyViewModel::class.java.simpleName ?: ""

    private val initCurrencyList: List<CurrencyUi> =
        currencyUiConverter.map(Currency.entries).sortedBy { it.name }

    private var debounceJob: Job? = null
    private val typedQuery = MutableStateFlow("")
    private var lastSearchQuery: String? = null
    private var searchResult = mutableListOf<CurrencyUi>()

    init {
        writableState.update { it.copy(currencyList = initCurrencyList) }
    }

    override fun obtainEvent(event: CurrencyEvent) {
        when (event) {
            is CurrencyEvent.OnBackClick -> onBackClick()
            is CurrencyEvent.OnSearchTextChanged -> onSearchTextChanged(event.newText)
            is CurrencyEvent.OnClearSearchLineClick -> onClearSearchLineClick()
            is CurrencyEvent.OnSearchStartClick -> searchCurrency(state.value.searchText)
            is CurrencyEvent.OnCurrencyClick -> onCurrencyClick(event.newCurrency)
        }
    }

    private fun onBackClick() {
        debounceJob?.cancel()
        writableAction.tryEmit(CurrencyAction.NavigateBack)
    }

    private fun onSearchTextChanged(newText: String) {
        typedQuery.value = newText
        writableState.update { it.copy(searchText = newText) }
        startDebounceWatcher()
    }

    private fun onClearSearchLineClick() {
        debounceJob?.cancel()
        lastSearchQuery = ""
        searchResult.clear()
        writableState.update {
            it.copy(
                searchText = "",
                currencyList = initCurrencyList
            )
        }
    }

    private fun onCurrencyClick(newCurrency: CurrencyUi) {
        if (state.value.selectedCurrencyId != newCurrency.currency.id) {
            launchWithErrorHandling(
                errorMessage = SET_REPORTING_CURRENCY_UNEXPECTED_ERROR,
                onError = {
                    writableAction.emit(
                        CurrencyAction.SetCurrencyError(newCurrency.currency)
                    )
                }
            ) {
                // TODO() проверить работает ли api с этой валютой
                setReportingCurrencyUseCase.execute(newCurrency.currency)
                writableAction.emit(CurrencyAction.NavigateBack)
            }
        }
    }

    private fun searchCurrency(query: String) {
        debounceJob?.cancel()

        if (query.isEmpty()) {
            lastSearchQuery = null
            searchResult.clear()
            writableState.update { it.copy(currencyList = initCurrencyList) }
            return
        }

        if (query == lastSearchQuery) {
            return
        }

        lastSearchQuery = query
        searchResult.clear()

        searchResult = initCurrencyList.filter { item ->
            item.name.contains(lastSearchQuery ?: "", ignoreCase = true) ||
                    item.currency.code.contains(lastSearchQuery ?: "", ignoreCase = true)
        }.toMutableList()

        // если к моменту получения результата запрос изменился
        if (query != lastSearchQuery) {
            searchResult.clear()
        } else {
            writableState.update {
                it.copy(
                    currencyList = searchResult
                )
            }
        }
    }


    @OptIn(FlowPreview::class)
    private fun startDebounceWatcher() {
        debounceJob?.cancel()
        debounceJob = viewModelScope.launch {
            typedQuery.debounce(SEARCH_DEBOUNCE)
                .distinctUntilChanged()
                .collectLatest { query ->
                    searchCurrency(query)
                }
        }
    }

    companion object {
        private const val SET_REPORTING_CURRENCY_UNEXPECTED_ERROR =
            "Error_setting_reporting_currency"
        private const val SEARCH_DEBOUNCE = 1000L
    }
}
