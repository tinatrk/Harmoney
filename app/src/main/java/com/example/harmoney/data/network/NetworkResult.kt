package com.example.harmoney.data.network

import java.io.IOException

sealed interface NetworkResult<out T> {

    data class Success<T>(
        val data: T,
    ) : NetworkResult<T>

    data class HttpError(
        val code: Int,
        val message: String? = null,
    ) : NetworkResult<Nothing>

    data class ConnectionError(
        val cause: IOException,
    ) : NetworkResult<Nothing>

    data class UnexpectedError(
        val cause: Throwable
    ) : NetworkResult<Nothing>
}
