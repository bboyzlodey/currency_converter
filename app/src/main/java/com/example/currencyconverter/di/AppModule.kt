package com.example.currencyconverter.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.currencyconverter.core.data.source.local.AppDatabase
import com.example.currencyconverter.core.data.source.local.CurrencyDao
import com.example.currencyconverter.core.network.CurrencyApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideCurrencyWebApi(): CurrencyApiService {

        return Retrofit.Builder()
            .client( OkHttpClient.Builder().build())
            .baseUrl("https://open.exchangerate-api.com/v6/latest/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(CurrencyApiService::class.java)
    }

    @Singleton
    @Provides
    fun sharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("currency_rates_cache", Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "currency.db").build()
    }

    @Provides
    fun provideCurrencyDao(appDatabase: AppDatabase) : CurrencyDao {
        return appDatabase.currencyDao()
    }
}