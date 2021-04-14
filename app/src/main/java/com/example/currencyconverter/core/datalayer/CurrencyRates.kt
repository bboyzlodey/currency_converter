package com.example.currencyconverter.core.datalayer

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CurrencyRates(
    @Json(name = "base_code") val baseCurrency: String = "USD",
    @Json(name = "rates") val rates: Map<String, Double>,
    @Json(name = "time_next_update_utc") val nextUpdate: String = ""
)