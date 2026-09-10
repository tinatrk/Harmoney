package com.example.harmoney.presentation.currency.models

import androidx.compose.runtime.Stable
import com.example.harmoney.presentation.models.CurrencyUi
import kotlinx.collections.immutable.persistentListOf

@Stable
data class CurrencyState(
    val searchText: String = "",
    val currencyList: List<CurrencyUi> = persistentListOf(),
    val selectedCurrencyId: Long
)
