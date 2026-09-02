package com.example.harmoney.presentation.category.models

sealed interface CategoryAction {
    data object NavigateBack : CategoryAction
    data object DataLoadingError : CategoryAction
    data object CheckingCategoryAlreadyExistsError : CategoryAction
    data object SaveCategoryError : CategoryAction
    data object CreateCategoryError : CategoryAction
    data object UpdateCategoryError : CategoryAction
    data object DeleteCategoryError : CategoryAction
    data class CreatedSuccessfully(val name: String) : CategoryAction
    data object UpdatedSuccessfully : CategoryAction
    data class DeletedSuccessfully(val name: String) : CategoryAction
}
