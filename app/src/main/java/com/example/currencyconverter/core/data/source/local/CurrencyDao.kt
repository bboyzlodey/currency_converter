package com.example.currencyconverter.core.data.source.local

import androidx.room.*

@Dao
interface CurrencyDao {
    @Query("SELECT * FROM currency_rates ORDER BY code DESC")
    suspend fun getAll() : List<DBCurrency>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg rates: DBCurrency)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(rates: List<DBCurrency>)
}