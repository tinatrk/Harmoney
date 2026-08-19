package com.example.harmoney.data.transaction.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.harmoney.data.core.TransactionWithCategory
import com.example.harmoney.data.transaction.dto.CategoryStatisticsDb
import com.example.harmoney.data.transaction.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    // При смене валюты
    @Query(
        "UPDATE `transaction` " +
                "SET amount = `transaction`.amount * :currencyExchangedCoeff"
    )
    suspend fun updateAmount(currencyExchangedCoeff: Long)

    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)

    @Query("DELETE FROM `transaction` WHERE dateMillis < :firstDayOfPeriodMillis")
    suspend fun deleteTransactionListOutOfRange(firstDayOfPeriodMillis: Long)

    @Query(
        """
            SELECT *
            FROM transaction_with_category_view
            WHERE id = :id
        """
    )
    suspend fun getTransaction(id: Long): TransactionWithCategory?

    @Query(
        """
            SELECT *
            FROM transaction_with_category_view
            WHERE category_typeId = :categoryTypeId
                AND dateMillis BETWEEN :firstDayOfPeriodMillis
                                   AND :lastDayOfPeriodMillis
                AND ( :categoryId IS NULL OR categoryId = :categoryId)
    """
    )
    fun getTransactionListWithCategory(
        categoryTypeId: Long,
        firstDayOfPeriodMillis: Long,
        lastDayOfPeriodMillis: Long,
        categoryId: Long?
    ): Flow<List<TransactionWithCategory>>

    @Query(
        """
            SELECT COALESCE(SUM(amount), 0)
        FROM transaction_with_category_view
        WHERE category_typeId = :categoryTypeId
            AND dateMillis BETWEEN :firstDayOfPeriodMillis
                               AND :lastDayOfPeriodMillis
            AND (:categoryId IS NULL OR categoryId = :categoryId)
    """
    )
    fun getTotalAmount(
        categoryTypeId: Long,
        firstDayOfPeriodMillis: Long,
        lastDayOfPeriodMillis: Long,
        categoryId: Long?
    ): Flow<Long>

    @Query(
        """SELECT
            category_id as id,
            category_name as name,
            category_typeId as typeId,
            category_iconId as iconId,
            category_iconColorId as iconColorId,
            category_createdAt as createdAt,
            category_userOrder as userOrder,
            
            SUM(amount) AS totalAmount,
            
            SUM(amount)
                / SUM(SUM(amount)) OVER() * 100
                AS percentage
            
        FROM transaction_with_category_view
        WHERE category_typeId = :categoryTypeId
            AND dateMillis BETWEEN :firstDayOfPeriodMillis
                               AND :lastDayOfPeriodMillis
        GROUP BY category_id
        """
    )
    fun getCategoryStatisticsList(
        categoryTypeId: Long,
        firstDayOfPeriodMillis: Long,
        lastDayOfPeriodMillis: Long,
    ): Flow<List<CategoryStatisticsDb>>
}
