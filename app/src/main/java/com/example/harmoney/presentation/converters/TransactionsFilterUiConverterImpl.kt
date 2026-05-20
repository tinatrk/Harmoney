package com.example.harmoney.presentation.converters

import com.example.harmoney.domain.models.TransactionsFilter
import com.example.harmoney.presentation.models.TransactionsFilterUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

class TransactionsFilterUiConverterImpl : TransactionsFilterUiConverter {
    override fun map(filter: TransactionsFilter): TransactionsFilterUi {
        return TransactionsFilterUi(id = filter.id, name = filter.name)
    }

    override fun map(filters: List<TransactionsFilter>): ImmutableList<TransactionsFilterUi> {
        return filters.map { map(it) }.toImmutableList()
    }
}
