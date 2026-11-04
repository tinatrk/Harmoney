package com.example.harmoney.presentation.currency.models

import androidx.compose.runtime.Stable
import com.example.harmoney.presentation.models.ContentState
import com.example.harmoney.presentation.models.CurrencyUi

/** `refreshCurrencyListId` - Номер обновления списка
 * (нужен для прокрутки списка в начало при изменениях) */
@Stable
data class CurrencyState(
    val searchText: String = "",
    val selectedCurrencyId: Long,
    /** Номер обновления списка (нужен для прокрутки списка в начало при изменениях) */
    val refreshCurrencyListId: Int = 0,
    val content: ContentState<List<CurrencyUi>, CurrencyScreenError> = ContentState.Idle,
)

enum class CurrencyScreenError {
    NETWORK_CONNECTION_ERROR,
    UPDATE_DATA_ERROR,
}
