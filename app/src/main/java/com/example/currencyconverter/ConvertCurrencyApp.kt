package com.example.currencyconverter

import android.app.Application
import com.example.currencyconverter.data.remote.CurrencyApiService
import dagger.hilt.android.HiltAndroidApp
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class ConvertCurrencyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

}