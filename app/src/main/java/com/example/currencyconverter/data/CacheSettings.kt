package com.example.currencyconverter.data

import android.content.SharedPreferences
import javax.inject.Inject

class CacheSettings @Inject constructor() {

    companion object {
        const val RATES_UPDATING = "rates_update"
    }
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    var nextCurrencyRateUpdating: String?
        get() = sharedPreferences.getString(RATES_UPDATING, null)
        set(value) = sharedPreferences.edit().putString(RATES_UPDATING, value).apply()
}