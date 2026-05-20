package com.example.harmoney.presentation.converters

import com.example.harmoney.domain.models.TransactionsFilter
import com.example.harmoney.presentation.models.TransactionsFilterUi
import kotlinx.collections.immutable.ImmutableList

sealed interface TransactionsFilterUiConverter {
    fun map(filter: TransactionsFilter): TransactionsFilterUi

    fun map(filters: List<TransactionsFilter>): ImmutableList<TransactionsFilterUi>

}
