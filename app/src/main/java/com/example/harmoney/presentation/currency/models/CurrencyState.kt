package com.example.harmoney.presentation.currency.models

import androidx.compose.runtime.Stable
import com.example.harmoney.presentation.models.ContentState
import com.example.harmoney.presentation.models.CurrencyUi
import kotlinx.collections.immutable.persistentListOf

/** `refreshCurrencyListId` - Номер обновления списка
 * (нужен для прокрутки списка в начало при изменениях) */
@Stable
data class CurrencyState(
    val searchText: String = "",
    val currencyList: List<CurrencyUi> = persistentListOf(),
    val selectedCurrencyId: Long,
    /** Номер обновления списка (нужен для прокрутки списка в начало при изменениях) */
    val refreshCurrencyListId: Int = 0,
    val contentState: ContentState = ContentState.IDLE
)
