package com.example.harmoney.data.category.impl

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
import timber.log.Timber

class CategoryRepositoryImpl(
    private val categoryDao: CategoryDao,
    private val categoryDBConverter: CategoryDBConverter,
) : CategoryRepository {
    // для корректной работы updateCategoryUserOrder
    private val sortingDirection = USER_ORDER_IN_ASCENDING_ORDER

    override suspend fun addCategory(category: Category): Resource<Unit, CategoryFailure> {
        return try {
            categoryDao.insertCategoryWithAutoUserOrder(
                category = categoryDBConverter.map(category),
                userOrderStep = USER_ORDER_STEP,
            )
            Resource.Success(Unit)
        } catch (e: SQLiteException) {
            Timber.e(e, "Error when inserting category with auto user order")
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
            Timber.e(e, "Error when getting category (checkCategoryAlreadyExists)")
            Resource.Error(CategoryFailure.DatabaseError)
        }
    }

    override suspend fun deleteCategory(category: Category): Resource<Unit, CategoryFailure> {
        return try {
            categoryDao.deleteCategory(categoryDBConverter.map(category))
            Resource.Success(Unit)
        } catch (e: SQLiteException) {
            Timber.e(e, "Error when deleting category")
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
            Timber.e("Error when getting category")
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
            Timber.e(e, "Error when getting categoryList")
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
            Timber.e(e, "Error when updating category")
            Resource.Error(CategoryFailure.DatabaseError)
        }
    }

    /** `categoryId` - id `category`, которая была размещена на новую позицию в списке
     *
     * `prevCategory` - элемент списка, идущий до `сategory` (выше/левее) на ее новой позиции
     * (независимо от направления сортировки)
     *
     * `nextCategory` - элемент списка, идущий после `сategory` (ниже/правее) на ее новой позиции
     *  (независимо от направления сортировки)
     * */
    override suspend fun updateCategoryUsedOrder(
        categoryId: Long,
        prevCategory: Category?,
        nextCategory: Category?
    ): Resource<Unit, CategoryFailure> {
        return try {
            val newUserOrder = when {
                // если список сategories пуст и теперь сategory первый элемент списка
                prevCategory == null && nextCategory == null -> USER_ORDER_STEP
                // если сategory переместили в самый верх списка
                prevCategory == null -> nextCategory!!.userOrder - sortingDirection * USER_ORDER_STEP
                // если сategory переместили в самый низ списка
                nextCategory == null -> prevCategory.userOrder + sortingDirection * USER_ORDER_STEP
                // если сategory переместили на позицию между двумя другими сategories
                else -> (prevCategory.userOrder + nextCategory.userOrder) / 2.0
            }
            categoryDao.updateCategoryUserOrder(categoryId, newUserOrder)
            Resource.Success(Unit)
        } catch (e: SQLiteException) {
            Timber.e(e, "Error when updating category user order")
            Resource.Error(CategoryFailure.DatabaseError)
        }
    }

    companion object {
        private const val USER_ORDER_STEP = 100.0
        private const val USER_ORDER_IN_ASCENDING_ORDER = 1 // значение не менять, это множитель
        private const val USER_ORDER_IN_DESCENDING_ORDER = -1 // значение не менять, это множитель
    }
}
