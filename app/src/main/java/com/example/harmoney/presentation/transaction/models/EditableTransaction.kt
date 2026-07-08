package com.example.harmoney.presentation.transaction.models

import com.example.harmoney.domain.models.CategoryType
import java.time.LocalDate

data class EditableTransaction(
    val amount: Double,
    val date: LocalDate,
    val note: String,
    val categoryId: Long,
    val categoryType: CategoryType
)
