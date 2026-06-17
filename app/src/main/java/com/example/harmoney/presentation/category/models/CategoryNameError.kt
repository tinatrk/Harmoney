package com.example.harmoney.presentation.category.models

sealed class CategoryNameError {
    data object None : CategoryNameError()
    data object Empty : CategoryNameError()
    data object AlreadyExists : CategoryNameError()
}
