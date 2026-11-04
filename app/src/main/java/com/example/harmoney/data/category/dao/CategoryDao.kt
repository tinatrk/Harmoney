package com.example.harmoney.data.category.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.harmoney.data.category.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity)

    @Update
    suspend fun updateCategory(category: CategoryEntity)

    @Delete
    suspend fun deleteCategory(category: CategoryEntity)

    @Query("SELECT * FROM category WHERE id = :id")
    suspend fun getCategory(id: Long): CategoryEntity?

    @Query("SELECT * FROM category WHERE name = :name AND typeId = :categoryTypeId")
    suspend fun getCategory(name: String, categoryTypeId: Long): CategoryEntity?

    @Query(
        """
        SELECT * FROM category 
        WHERE typeId = :categoryTypeId 
        ORDER BY name COLLATE LOCALIZED ASC
    """
    )
    fun getCategoryListSortedByName(categoryTypeId: Long): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM category WHERE typeId = :categoryTypeId ORDER BY createdAt ASC")
    fun getCategoryListSortedByCreatedAt(categoryTypeId: Long): Flow<List<CategoryEntity>>

    @Query(
        """
        SELECT * FROM category 
        WHERE typeId = :categoryTypeId 
        ORDER BY 
                CASE WHEN :ascending = 1 THEN userOrder END ASC,
                CASE WHEN :ascending = 0 THEN userOrder END DESC
    """
    )
    fun getCategoryListSortedByUserOrder(
        categoryTypeId: Long,
        ascending: Boolean
    ): Flow<List<CategoryEntity>>

    @Query("UPDATE category SET userOrder = :newUserOrder WHERE id = :categoryId")
    suspend fun updateCategoryUserOrder(categoryId: Long, newUserOrder: Double)

    @Query("SELECT MAX(userOrder) FROM category WHERE typeId = :categoryTypeId")
    suspend fun getMaxUserOrderForOneType(categoryTypeId: Long): Double?

    /** Сохраняет `category`, задавая ей самый большой `userOrder` среди всех categories.
     * Т.е. category будет в конце списка при сортировке по возрастанию
     * или в начале списка при сортировке по убыванию */
    @Transaction
    suspend fun insertCategoryWithAutoUserOrder(
        category: CategoryEntity,
        userOrderStep: Double,
    ) {
        val maxUserOrder = getMaxUserOrderForOneType(category.typeId) ?: 0.0
        val newUserOrder = maxUserOrder + userOrderStep

        insertCategory(category.copy(userOrder = newUserOrder))
    }

    @Query("DELETE FROM category WHERE typeId = :categoryTypeId")
    suspend fun deleteAllCategoriesOfType(categoryTypeId: Long)
}
