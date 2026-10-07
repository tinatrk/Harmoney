package com.example.harmoney.core.database

import androidx.room.TypeConverter
import java.math.BigDecimal

object BigDecimalConverter {

    @TypeConverter
    fun fromBigDecimal(value: BigDecimal?): Long? = value?.toLong()

    @TypeConverter
    fun toBigDecimal(value: Long?): BigDecimal? = value?.toBigDecimal()
}
