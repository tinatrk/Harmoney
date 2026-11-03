package com.example.harmoney.core.util

sealed interface AsyncResult<out T, out E> {

    data object Loading : AsyncResult<Nothing, Nothing>

    data class Success<T>(val data: T) : AsyncResult<T, Nothing>

    data class Error<E>(val error: E) : AsyncResult<Nothing, E>
}

inline fun <T, R, E> AsyncResult<T, E>.mapSuccess(
    transform: (T) -> R
): AsyncResult<R, E> = when (this) {
    is AsyncResult.Loading -> AsyncResult.Loading
    is AsyncResult.Error -> AsyncResult.Error(error)
    is AsyncResult.Success -> AsyncResult.Success(transform(data))
}
