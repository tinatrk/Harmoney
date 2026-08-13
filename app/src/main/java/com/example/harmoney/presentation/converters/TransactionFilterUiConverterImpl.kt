package com.example.harmoney.presentation.converters

import com.example.harmoney.domain.models.TransactionFilter
import com.example.harmoney.presentation.models.TransactionFilterUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

class TransactionFilterUiConverterImpl : TransactionFilterUiConverter {
    override fun map(filter: TransactionFilter): TransactionFilterUi {
        return when (filter) {
            is TransactionFilter.All -> TransactionFilterUi.All
            is TransactionFilter.Category -> TransactionFilterUi.CategoryUi(
                id = filter.id,
                name = filter.name
            )
        }
    }

    override fun map(filters: List<TransactionFilter>): ImmutableList<TransactionFilterUi> {
        return filters.map { map(filter = it) }.toImmutableList()
    }
}
