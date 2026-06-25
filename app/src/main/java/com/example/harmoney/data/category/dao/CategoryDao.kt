package com.example.harmoney.data.category.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.harmoney.data.category.entity.CategoryEntity

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.NONE)
    suspend fun insertCategory(category: CategoryEntity)

    @Update
    suspend fun updateCategory(category: CategoryEntity)

    @Delete
    suspend fun deleteCategory(category: CategoryEntity)

    @Query("SELECT * FROM category WHERE id = :id")
    suspend fun getCategory(id: Long): CategoryEntity?

    @Query("SELECT * FROM category WHERE name = :name AND typeId = :categoryTypeId")
    suspend fun getCategory(name: String, categoryTypeId: Long) : CategoryEntity?

    @Query("SELECT * FROM category WHERE typeId = :categoryTypeId")
    suspend fun getCategoryList(categoryTypeId: Long): List<CategoryEntity>?

    // Если вставляем категорию в конец списка
    @Query("SELECT MAX(userOrder) FROM category WHERE typeId = :categoryTypeId")
    suspend fun getMaxUserOrderForOneType(categoryTypeId: Long): Double?

    // Если вставляем категорию в начало списка
    @Query("SELECT MIN(userOrder) FROM category WHERE typeId = :categoryTypeId")
    suspend fun getMinUserOrderForOneType(categoryTypeId: Long): Double?

    @Transaction
    suspend fun insertCategoryWithAutoUserOrder(
        category: CategoryEntity,
        userOrderStep: Double,
        isInsertToEndOfList: Boolean
    ) {
        val newUserOrder = if (isInsertToEndOfList) {
            val maxUserOrder = getMaxUserOrderForOneType(category.typeId) ?: 0.0
            maxUserOrder + userOrderStep
        } else {
            val minUserOrder = getMinUserOrderForOneType(category.typeId) ?: 0.0
            minUserOrder - userOrderStep
        }
        insertCategory(category.copy(userOrder = newUserOrder))
    }

    @Query("DELETE FROM category WHERE typeId = :categoryTypeId")
    suspend fun deleteAllCategoriesOfType(categoryTypeId: Long)
}
