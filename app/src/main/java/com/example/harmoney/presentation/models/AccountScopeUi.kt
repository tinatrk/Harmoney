package com.example.harmoney.presentation.models

import androidx.compose.runtime.Immutable

@Immutable
data class AccountScopeUi(
    val id: Long,
    val name: String,
    val balance: String,
    val balanceOriginals: String?,
    val isArchivedMark: Boolean,
)
