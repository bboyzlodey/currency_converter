package com.example.currencyconverter.data

import com.example.currencyconverter.data.local.CurrencyDao
import com.example.currencyconverter.data.local.DBCurrency
import com.example.currencyconverter.data.remote.CurrencyApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class CurrencyRepository @Inject constructor() {

    @Inject lateinit var serverApi: CurrencyApiService
    @Inject lateinit var database: CurrencyDao
    @Inject lateinit var cacheSettings: CacheSettings


    fun getCurrencyRates() : Flow<List<DBCurrency>> {
        return database.getAll()
    }

    suspend fun fetchCurrencyRates() {
        val response = serverApi.getCurrencyRates()
        withContext(Dispatchers.Main) {
            cacheSettings.nextCurrencyRateUpdating = response.nextUpdate
        }
        //const
        Timber.i("updateLocalData")
        val local = response.rates.map {
            DBCurrency(
                it.key,
                it.value.toFloat()
            )
        }
        database.insertAll(local)
    }
}