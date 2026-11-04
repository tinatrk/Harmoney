package com.example.harmoney.domain.account.models

sealed interface AccountFailure {
    data object BadRequest : AccountFailure
    data object DatabaseError : AccountFailure
}
