package com.example.harmoney.presentation.transaction.models

import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.Money
import java.time.LocalDate

data class EditableTransaction(
    val amount: Money, // здесь всегда globalCurrency
    val date: LocalDate,
    val note: String,
    val categoryId: Long,
    val categoryType: CategoryType
)
