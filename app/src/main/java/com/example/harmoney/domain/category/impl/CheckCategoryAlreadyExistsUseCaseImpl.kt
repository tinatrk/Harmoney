package com.example.harmoney.domain.category.impl

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.category.api.reposiory.CategoryRepository
import com.example.harmoney.domain.category.api.useCase.CheckCategoryAlreadyExistsUseCase
import com.example.harmoney.domain.category.models.CategoryFailure
import com.example.harmoney.domain.models.CategoryType
import kotlinx.coroutines.flow.Flow

class CheckCategoryAlreadyExistsUseCaseImpl(private val repository: CategoryRepository) :
    CheckCategoryAlreadyExistsUseCase {
    override suspend fun execute(
        categoryName: String,
        categoryType: CategoryType
    ): Flow<Resource<Boolean, CategoryFailure>> {
        return repository.checkCategoryAlreadyExists(categoryName, categoryType)
    }
}
