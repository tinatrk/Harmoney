package com.example.harmoney.presentation.converters

import com.example.harmoney.domain.models.TransactionFilter
import com.example.harmoney.presentation.models.TransactionFilterUi
import kotlinx.collections.immutable.ImmutableList

sealed interface TransactionFilterUiConverter {
    fun map(filter: TransactionFilter): TransactionFilterUi

    fun map(filters: List<TransactionFilter>): ImmutableList<TransactionFilterUi>

}
