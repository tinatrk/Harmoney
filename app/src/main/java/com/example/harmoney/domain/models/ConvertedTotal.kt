package com.example.harmoney.domain.models

data class ConvertedTotal(
    val total: Money,
    val includedAmounts: List<ConvertedMoney>,
    val excludedAmounts: List<Money>,
)
