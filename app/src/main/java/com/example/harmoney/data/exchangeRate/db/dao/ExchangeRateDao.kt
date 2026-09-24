package com.example.harmoney.data.exchangeRate.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.harmoney.data.exchangeRate.db.entity.ExchangeRateEntity
import com.example.harmoney.data.exchangeRate.db.entity.ExchangeRateSyncEntity

@Dao
interface ExchangeRateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRate(rate: ExchangeRateEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRates(rates: List<ExchangeRateEntity>)

    @Query(
        """
        SELECT *
        FROM exchange_rates
        WHERE baseCurrencyId = :baseCurrencyId
            AND quoteCurrencyId IN (:quoteCurrencyIds)
            AND dateEpochDay = :dateEpochDay
        LIMIT 1
    """
    )
    suspend fun getRatesByDate(
        baseCurrencyId: Long,
        quoteCurrencyIds: List<Long>,
        dateEpochDay: Long,
    ): List<ExchangeRateEntity>

    @Query(
        """
        SELECT *
        FROM exchange_rates AS er
        WHERE er.baseCurrencyId = :baseCurrencyId
            AND er.quoteCurrencyId IN (:quoteCurrencyIds)
            AND er.dateEpochDay = (
                SELECT MAX(er2.dateEpochDay)
                FROM exchange_rates AS er2
                WHERE er2.baseCurrencyId = er.baseCurrencyId
                    AND er2.quoteCurrencyId = er.quoteCurrencyId
                    AND er2.dateEpochDay <= :dateEpochDay
            )
    """
    )
    suspend fun getRatesClosestToDate(
        baseCurrencyId: Long,
        quoteCurrencyIds: List<Long>,
        dateEpochDay: Long,
    ): List<ExchangeRateEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun markAsSynced(
        entities: List<ExchangeRateSyncEntity>
    )

    @Query(
        """
        SELECT quoteCurrencyId
        FROM exchange_rate_sync
        WHERE baseCurrencyId = :baseCurrencyId
            AND quoteCurrencyId IN (:quoteCurrencyIds)
            AND dateEpochDay = :dateEpochDay
    """
    )
    suspend fun getSyncedCurrencyIds(
        baseCurrencyId: Long,
        quoteCurrencyIds: List<Long>,
        dateEpochDay: Long,
    ): List<Long>
}
