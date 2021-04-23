package com.example.currencyconverter.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.currencyconverter.data.local.AppDatabase
import com.example.currencyconverter.data.local.CurrencyDao
import com.example.currencyconverter.data.remote.CurrencyApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideCurrencyWebApi(): CurrencyApiService =
        Retrofit.Builder()
            .client( OkHttpClient.Builder().build())
                //const
            .baseUrl("https://open.exchangerate-api.com/v6/latest/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(CurrencyApiService::class.java)

    @Singleton
    @Provides
    fun sharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        //const
        context.getSharedPreferences("currency_rates_cache", Context.MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        //const
         Room.databaseBuilder(context, AppDatabase::class.java, "currency.db").build()

    @Provides
    fun provideCurrencyDao(appDatabase: AppDatabase) : CurrencyDao =
        appDatabase.currencyDao()
}