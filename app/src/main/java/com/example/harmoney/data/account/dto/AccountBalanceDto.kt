package com.example.harmoney.data.account.dto

data class AccountBalanceDto(
    val accountId: Long,
    val currencyId: Long,
    val balanceMinorUnits: Long,
)
