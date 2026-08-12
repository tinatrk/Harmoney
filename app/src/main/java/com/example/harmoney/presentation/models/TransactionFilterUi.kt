package com.example.harmoney.presentation.models

import androidx.compose.runtime.Immutable

@Immutable
sealed interface TransactionFilterUi {
    data object All : TransactionFilterUi

    data class CategoryUi(
        val id: Long,
        val name: String
    ) : TransactionFilterUi
}
