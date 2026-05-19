package com.example.harmoney.presentation.sharedViewModel

import androidx.lifecycle.ViewModel
import com.example.harmoney.domain.models.CategoryType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SharedCategoryTypeViewModel() : ViewModel() {
    private val _selectedCategoryType = MutableStateFlow(CategoryType.Expenses)
    val selectedCategoryType: StateFlow<CategoryType> = _selectedCategoryType.asStateFlow()

    fun categoryTypeChanged(newCategoryType: CategoryType) {
        if (selectedCategoryType.value.id != newCategoryType.id) {
            _selectedCategoryType.update {
                newCategoryType
            }
        }
    }
}
