package com.example.harmoney.domain.transaction.models

import com.example.harmoney.core.util.Failure

sealed interface TransactionFailure : Failure {
    data object BadRequest : TransactionFailure
    data object DatabaseError : TransactionFailure
    data class Unknown(val cause: Throwable? = null) : TransactionFailure
}
