package com.example.currencyconverter.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency_rates")
data class DBCurrency (
    @PrimaryKey var code: String = "",
    var fraction: Float = 0f
)