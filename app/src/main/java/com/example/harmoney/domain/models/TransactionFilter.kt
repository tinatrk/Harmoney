package com.example.harmoney.domain.models

sealed interface TransactionFilter {
    data object All : TransactionFilter

    data class Category(
        val id: Long,
        val name: String,
    ) : TransactionFilter
}
