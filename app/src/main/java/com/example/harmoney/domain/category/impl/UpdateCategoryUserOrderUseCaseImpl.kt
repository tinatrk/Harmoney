package com.example.harmoney.domain.category.impl

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.category.api.reposiory.CategoryRepository
import com.example.harmoney.domain.category.api.useCase.UpdateCategoryUserOrderUseCase
import com.example.harmoney.domain.category.models.CategoryFailure
import com.example.harmoney.domain.models.Category

class UpdateCategoryUserOrderUseCaseImpl(private val repository: CategoryRepository) :
    UpdateCategoryUserOrderUseCase {
    override suspend fun execute(
        from: Int,
        to: Int,
        oldCategories: List<Category>
    ): Resource<Unit, CategoryFailure> {
        if (from == to) return Resource.Success(Unit)

        val newList: MutableList<Category> = oldCategories.toMutableList()
        val item = newList.removeAt(from)
        newList.add(to, item)

        val prev = newList.getOrNull(to - 1)
        val next = newList.getOrNull(to + 1)

        return repository
            .updateCategoryUsedOrder(item.id, prev, next)
    }
}
