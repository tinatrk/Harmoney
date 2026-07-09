package com.example.harmoney.data.transaction.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.harmoney.data.core.TransactionWithCategory
import com.example.harmoney.data.transaction.entity.TransactionEntity

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
    suspend fun updateAmount(currencyExchangedCoeff: Double)

    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)

    @Query("DELETE FROM `transaction` WHERE dateMillis < :firstDayOfPeriodMillis")
    suspend fun deleteTransactionListOutOfRange(firstDayOfPeriodMillis: Long)

    @Query("SELECT * FROM `transaction` WHERE id = :id")
    suspend fun getTransaction(id: Long): TransactionEntity?

    @Query(
        """SELECT `transaction`.*,
        category.id AS category_id,
        category.name AS category_name,
        category.typeId AS category_typeId,
        category.iconId AS category_iconId,
        category.iconColorId AS category_iconColorId,
        category.createdAt AS category_createdAt,
        category.userOrder AS category_userOrder
        FROM `transaction`
        INNER JOIN category ON `transaction`.categoryId = category.id
        WHERE category.typeId = :categoryTypeId 
            AND `transaction`.dateMillis >= :firstDayOfPeriodMillis
            AND `transaction`.dateMillis <= :lastDayOfPeriodMillis
    """
    )
    suspend fun getTransactionListWithCategoryByTypeAndPeriod(
        categoryTypeId: Long,
        firstDayOfPeriodMillis: Long,
        lastDayOfPeriodMillis: Long,
    ): List<TransactionWithCategory>?

    @Query(
        """SELECT COALESCE(SUM(`transaction`.amount), 0.0)
        FROM `transaction`
        INNER JOIN category ON `transaction`.categoryId = category.id
        WHERE category.typeId = :categoryTypeId
            AND `transaction`.dateMillis >= :firstDayOfPeriodMillis
            AND `transaction`.dateMillis <= :lastDayOfPeriodMillis
    """
    )
    suspend fun getTotalAmountByTypeAndPeriod(
        categoryTypeId: Long,
        firstDayOfPeriodMillis: Long,
        lastDayOfPeriodMillis: Long,
    ): Double
}
