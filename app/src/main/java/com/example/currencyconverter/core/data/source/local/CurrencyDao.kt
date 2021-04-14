package com.example.currencyconverter.core.data.source.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {
    @Query("SELECT * FROM currency_rates ORDER BY code DESC")
    fun getAll() : Flow< List<DBCurrency>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg rates: DBCurrency)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(rates: List<DBCurrency>)
}