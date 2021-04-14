package com.example.currencyconverter.core.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DBCurrency::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao
}