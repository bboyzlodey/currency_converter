package com.example.currencyconverter.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DBCurrency::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao
}