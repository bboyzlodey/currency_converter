package com.example.currencyconverter.core

import android.app.Application
import com.example.currencyconverter.core.network.CurrencyApiService
import dagger.hilt.android.HiltAndroidApp
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class ConvertCurrencyApp : Application() {
    private val BASE_URL = "https://open.exchangerate-api.com/v6/latest/USD/"
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

    @Inject
    fun provideCurrencyApiService() : CurrencyApiService{
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        return retrofit.create(CurrencyApiService::class.java)
    }
}