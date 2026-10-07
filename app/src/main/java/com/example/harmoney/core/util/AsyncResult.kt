package com.example.harmoney.core.util

sealed interface AsyncResult<out T, out E> {

    data object Loading : AsyncResult<Nothing, Nothing>

    data class Success<T>(val data: T) : AsyncResult<T, Nothing>

    data class Error<E>(val error: E) : AsyncResult<Nothing, E>
}
