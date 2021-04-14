package com.example.currencyconverter.core.data.source

import com.example.currencyconverter.core.datalayer.CurrencyRates

interface CurrencyDataSource {
    fun getRates() : CurrencyRates
}