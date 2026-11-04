package com.example.harmoney.presentation.models

sealed interface ContentState<out T, out E> {
    data object Idle : ContentState<Nothing, Nothing>

    data object Loading : ContentState<Nothing, Nothing>

    data class Error<E>(val errorType: E) : ContentState<Nothing, E>

    data class Success<T>(val content: T) : ContentState<T, Nothing>
}
