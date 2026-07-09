package com.example.harmoney.domain.category.impl

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.category.api.reposiory.CategoryRepository
import com.example.harmoney.domain.category.api.useCase.DeleteCategoryUseCase
import com.example.harmoney.domain.category.models.CategoryFailure
import com.example.harmoney.domain.models.Category

class DeleteCategoryUseCaseImpl(private val repository: CategoryRepository) :
    DeleteCategoryUseCase {
    override suspend fun execute(category: Category): Resource<Unit, CategoryFailure> {
        return repository.deleteCategory(category)
    }
}
