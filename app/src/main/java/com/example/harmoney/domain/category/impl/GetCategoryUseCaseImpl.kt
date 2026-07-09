package com.example.harmoney.domain.category.impl

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.category.api.reposiory.CategoryRepository
import com.example.harmoney.domain.category.api.useCase.GetCategoryUseCase
import com.example.harmoney.domain.category.models.CategoryFailure
import com.example.harmoney.domain.models.Category

class GetCategoryUseCaseImpl(private val repository: CategoryRepository) : GetCategoryUseCase {
    override suspend fun execute(categoryId: Long): Resource<Category, CategoryFailure> {
        return repository.getCategory(categoryId)
    }
}
