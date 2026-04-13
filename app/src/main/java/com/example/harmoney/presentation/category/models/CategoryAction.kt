package com.example.harmoney.presentation.category.models

sealed interface CategoryAction {
    data object NavigateBack : CategoryAction
}
