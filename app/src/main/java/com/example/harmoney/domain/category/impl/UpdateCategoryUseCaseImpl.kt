package com.example.harmoney.domain.category.impl

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.category.api.reposiory.CategoryRepository
import com.example.harmoney.domain.category.api.useCase.UpdateCategoryUseCase
import com.example.harmoney.domain.category.models.CategoryFailure
import com.example.harmoney.domain.models.Category

class UpdateCategoryUseCaseImpl(private val repository: CategoryRepository) :
    UpdateCategoryUseCase {
    override suspend fun execute(category: Category): Resource<Unit, CategoryFailure> {
        return repository.updateCategory(category)
    }
}
