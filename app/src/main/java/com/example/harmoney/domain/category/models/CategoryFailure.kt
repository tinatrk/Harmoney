package com.example.harmoney.domain.category.models

import com.example.harmoney.core.util.Failure

sealed interface CategoryFailure : Failure {
    data object BadRequest : CategoryFailure
    data class Unknown(val cause: Throwable? = null) : CategoryFailure
}
