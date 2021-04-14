package com.example.currencyconverter.core.network

import com.example.currencyconverter.core.datalayer.CurrencyRates
import retrofit2.http.GET

interface CurrencyApiService {

    @GET(value = "USD")
    suspend fun getCurrencyRates() : CurrencyRates
}