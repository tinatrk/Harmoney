package com.example.harmoney.data.category.impl

import android.util.Log
import androidx.sqlite.SQLiteException
import com.example.harmoney.core.util.Resource
import com.example.harmoney.data.category.converter.CategoryDBConverter
import com.example.harmoney.data.category.dao.CategoryDao
import com.example.harmoney.domain.category.api.reposiory.CategoryRepository
import com.example.harmoney.domain.category.models.CategoryFailure
import com.example.harmoney.domain.models.Category
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.SortOption
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class CategoryRepositoryImpl(
    private val categoryDao: CategoryDao,
    private val categoryDBConverter: CategoryDBConverter,
) : CategoryRepository {
    // для корректной работы updateCategoryUserOrder
    private val sortingDirection = USER_ORDER_IN_ASCENDING_ORDER

    override suspend fun addCategory(category: Category): Resource<Unit, CategoryFailure> {
        return try {
            categoryDao.insertCategory(categoryDBConverter.map(category))
            Resource.Success(Unit)
        } catch (e: SQLiteException) {
            Log.e(DATABASE_TAG, "Error when inserting category: ${e.message}", e)
            Resource.Error(CategoryFailure.DatabaseError)
        }
    }

    override suspend fun checkCategoryAlreadyExists(
        categoryName: String,
        categoryType: CategoryType
    ): Resource<Boolean, CategoryFailure> {
        return try {
            val category = categoryDao.getCategory(categoryName, categoryType.id)
            Resource.Success(category != null)
        } catch (e: SQLiteException) {
            Log.e(
                DATABASE_TAG,
                "Error when getting category (checkCategoryAlreadyExists): ${e.message}",
                e
            )
            Resource.Error(CategoryFailure.DatabaseError)
        }
    }

    override suspend fun deleteCategory(category: Category): Resource<Unit, CategoryFailure> {
        return try {
            categoryDao.deleteCategory(categoryDBConverter.map(category))
            Resource.Success(Unit)
        } catch (e: SQLiteException) {
            Log.e(DATABASE_TAG, "Error when deleting category: ${e.message}", e)
            Resource.Error(CategoryFailure.DatabaseError)
        }
    }

    override suspend fun getCategory(categoryId: Long): Resource<Category, CategoryFailure> {
        return try {
            val category = categoryDao.getCategory(categoryId)
            if (category != null) {
                Resource.Success(categoryDBConverter.map(category))
            } else {
                Resource.Error(CategoryFailure.BadRequest)
            }
        } catch (e: SQLiteException) {
            Log.e(DATABASE_TAG, "Error when getting category: ${e.message}", e)
            Resource.Error(CategoryFailure.DatabaseError)
        }
    }

    override fun getCategoryList(categoryType: CategoryType, sortOption: SortOption)
            : Flow<Resource<List<Category>, CategoryFailure>> {
        val categoriesFlow = when (sortOption) {
            SortOption.ALPHABET -> {
                categoryDao.getCategoryListSortedByName(categoryType.id)
            }

            SortOption.TIME_CREATED -> {
                categoryDao.getCategoryListSortedByCreatedAt(categoryType.id)
            }

            SortOption.USER_ORDER -> {
                categoryDao.getCategoryListSortedByUserOrder(
                    categoryTypeId = categoryType.id,
                    ascending = sortingDirection == USER_ORDER_IN_ASCENDING_ORDER
                )
            }
        }

        return categoriesFlow.map { entities ->
            Resource.Success(categoryDBConverter.map(entities))
                    as Resource<List<Category>, CategoryFailure>
        }.catch { e ->
            Log.e(DATABASE_TAG, "Error when getting categoryList: ${e.message}", e)
            when (e) {
                is SQLiteException -> emit(Resource.Error(CategoryFailure.DatabaseError))
                else -> throw e
            }
        }
    }

    override suspend fun updateCategory(category: Category): Resource<Unit, CategoryFailure> {
        return try {
            categoryDao.updateCategory(categoryDBConverter.map(category))
            Resource.Success(Unit)
        } catch (e: SQLiteException) {
            Log.e(DATABASE_TAG, "Error when updating category: ${e.message}", e)
            Resource.Error(CategoryFailure.DatabaseError)
        }
    }

    override suspend fun updateCategoryUsedOrder(
        categoryId: Long,
        prevCategory: Category?,
        nextCategory: Category?
    ): Resource<Unit, CategoryFailure> {
        return try {
            val newUserOrder = when {
                prevCategory == null && nextCategory == null -> USER_ORDER_STEP
                prevCategory == null -> {
                    nextCategory!!.userOrder - sortingDirection * USER_ORDER_STEP
                }

                nextCategory == null -> prevCategory.userOrder + sortingDirection * USER_ORDER_STEP
                else -> (prevCategory.userOrder + nextCategory.userOrder) / 2.0
            }
            categoryDao.updateCategoryUserOrder(categoryId, newUserOrder)
            Resource.Success(Unit)
        } catch (e: SQLiteException) {
            Log.e(
                DATABASE_TAG,
                "Error when updating category user order: ${e.message}",
                e
            )
            Resource.Error(CategoryFailure.DatabaseError)
        }
    }

    companion object {
        private const val DATABASE_TAG = "HarmApp_CategoryDB"
        private const val USER_ORDER_STEP = 100.0
        private const val USER_ORDER_IN_ASCENDING_ORDER = 1 // значение не менять
        private const val USER_ORDER_IN_DESCENDING_ORDER = -1 // значение не менять
    }
}
