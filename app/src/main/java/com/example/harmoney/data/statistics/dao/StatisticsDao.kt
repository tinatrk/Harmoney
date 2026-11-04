package com.example.harmoney.data.statistics.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.harmoney.data.core.bd.dto.TotalMoneyDto
import com.example.harmoney.data.statistics.dto.CategoryStatisticsDb
import kotlinx.coroutines.flow.Flow

@Dao
interface StatisticsDao {
    @Query(
        """
            SELECT account_currencyId AS currencyId,
                    COALESCE(SUM(moneyAmountMinorUnits), 0) AS moneyAmountMinorUnits
                    
        FROM transaction_with_category_view
        
        WHERE account_id IN (:accountIds)
            AND category_typeId = :categoryTypeId
            AND dateMillis BETWEEN :firstDayOfPeriodMillis
                               AND :lastDayOfPeriodMillis
            AND (:categoryId IS NULL OR categoryId = :categoryId)
            
        GROUP BY account_currencyId
    """
    )
    fun observeTotalAmountByCategoryType(
        accountIds: List<Long>,
        categoryTypeId: Long,
        firstDayOfPeriodMillis: Long,
        lastDayOfPeriodMillis: Long,
        categoryId: Long? = null
    ): Flow<List<TotalMoneyDto>>

    @Query(
        """SELECT
            category_id as id,
            category_name as name,
            category_typeId as typeId,
            category_iconId as iconId,
            category_iconColorId as iconColorId,
            category_createdAt as createdAt,
            category_userOrder as userOrder,
            
            account_currencyId AS currencyId,            
            SUM(moneyAmountMinorUnits) AS moneyAmountMinorUnits
            
        FROM transaction_with_category_view
        
        WHERE category_typeId = :categoryTypeId
            AND account_id IN (:accountIds)
            AND dateMillis BETWEEN :firstDayOfPeriodMillis
                               AND :lastDayOfPeriodMillis
                               
        GROUP BY category_id, account_currencyId
        """
    )
    fun observeCategoryStatisticsList(
        categoryTypeId: Long,
        accountIds: List<Long>,
        firstDayOfPeriodMillis: Long,
        lastDayOfPeriodMillis: Long,
    ): Flow<List<CategoryStatisticsDb>>
}
