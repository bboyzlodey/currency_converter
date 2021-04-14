package com.example.currencyconverter.data.remote

import retrofit2.http.GET

interface CurrencyApiService {

    @GET(value = "USD")
    suspend fun getCurrencyRates() : CurrencyRates
}