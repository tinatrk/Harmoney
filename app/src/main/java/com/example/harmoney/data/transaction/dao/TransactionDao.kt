package com.example.harmoney.data.transaction.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.harmoney.data.core.bd.dto.TransactionDetailsDto
import com.example.harmoney.data.transaction.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

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
    suspend fun getTransaction(id: Long): TransactionDetailsDto?

    @Query(
        """
            SELECT *
            FROM transaction_with_category_view
            WHERE account_id IN (:accountIds)
                AND category_typeId = :categoryTypeId
                AND dateMillis BETWEEN :firstDayOfPeriodMillis
                                   AND :lastDayOfPeriodMillis
                AND ( :categoryId IS NULL OR categoryId = :categoryId)
            ORDER BY dateMillis DESC
    """
    )
    fun getTransactionList(
        accountIds: List<Long>,
        categoryTypeId: Long,
        firstDayOfPeriodMillis: Long,
        lastDayOfPeriodMillis: Long,
        categoryId: Long?
    ): Flow<List<TransactionDetailsDto>>
}
