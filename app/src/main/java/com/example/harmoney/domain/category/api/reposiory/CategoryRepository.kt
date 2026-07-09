package com.example.harmoney.domain.category.api.reposiory

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.category.models.CategoryFailure
import com.example.harmoney.domain.models.Category
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.SortOption
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    suspend fun addCategory(category: Category): Resource<Unit, CategoryFailure>

    suspend fun updateCategory(category: Category): Resource<Unit, CategoryFailure>

    suspend fun deleteCategory(category: Category): Resource<Unit, CategoryFailure>

    suspend fun getCategory(categoryId: Long): Resource<Category, CategoryFailure>

    fun getCategoryList(categoryType: CategoryType, sortOption: SortOption)
            : Flow<Resource<List<Category>, CategoryFailure>>

    suspend fun updateCategoryUsedOrder(
        categoryId: Long,
        prevCategory: Category?,
        nextCategory: Category?
    ): Resource<Unit, CategoryFailure>

    suspend fun checkCategoryAlreadyExists(
        categoryName: String,
        categoryType: CategoryType
    ): Resource<Boolean, CategoryFailure>
}
