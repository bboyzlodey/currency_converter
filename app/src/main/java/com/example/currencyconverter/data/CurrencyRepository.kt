package com.example.currencyconverter.data

import com.example.currencyconverter.data.local.CurrencyDao
import com.example.currencyconverter.data.local.DBCurrency
import com.example.currencyconverter.data.remote.CurrencyRates
import com.example.currencyconverter.data.remote.CurrencyApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class CurrencyRepository @Inject constructor() {

    @Inject lateinit var webApi: CurrencyApiService
    @Inject lateinit var db: CurrencyDao
    @Inject lateinit var cacheSettings: CacheSettings

    suspend fun getCurrencyRates() : CurrencyRates {
        return webApi.getCurrencyRates()
    }

    fun getLocalData() : Flow<List<DBCurrency>> {
        return db.getAll()
    }

    suspend fun updateLocalData() {
        val response = webApi.getCurrencyRates()
        withContext(Dispatchers.Main) {
            cacheSettings.nextCurrencyRateUpdating = response.nextUpdate
        }
        Timber.i("updateLocalData")
        val local = response.rates.map {
            DBCurrency(
                it.key,
                it.value.toFloat()
            )
        }
        db.insertAll(local)

    }
}