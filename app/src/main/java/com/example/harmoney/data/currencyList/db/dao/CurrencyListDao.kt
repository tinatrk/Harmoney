package com.example.harmoney.data.currencyList.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.harmoney.data.currencyList.db.entity.CurrencyEntity

@Dao
interface CurrencyListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(currencies: List<CurrencyEntity>)

    @Query("DELETE FROM supported_currencies")
    suspend fun deleteAll()

    @Query("SELECT * FROM  supported_currencies")
    suspend fun getCurrencyList(): List<CurrencyEntity>
}
