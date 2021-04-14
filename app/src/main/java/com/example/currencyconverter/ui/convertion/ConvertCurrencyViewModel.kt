package com.example.currencyconverter.ui.convertion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.core.data.source.CacheSettings
import com.example.currencyconverter.core.datalayer.CurrencyRepository
import com.example.currencyconverter.utils.DateTimeHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ConvertCurrencyViewModel @Inject constructor() : ViewModel() {

//    @Inject
//    lateinit var context: Context

    @Inject
    lateinit var dateTimeHelper: DateTimeHelper

    @Inject
    lateinit var currencyRepo: CurrencyRepository

    @Inject
    lateinit var cacheSettings: CacheSettings

    fun onSwapClicked() {
        Timber.i("onSwipedClicked!")
        val localDataExpired =
            cacheSettings.nextCurrencyRateUpdating?.let { dateTimeHelper.dateTimeBefore(it) }
                ?: false
        Timber.d("localDataExpired = $localDataExpired")
        Timber.d("Cache timestamp: ${cacheSettings.nextCurrencyRateUpdating}")
        if (localDataExpired) {
            viewModelScope.launch {
                try {
                    currencyRepo.updateLocalData()
                    val datas = currencyRepo.getLocalData()
                    Timber.d("Data is: $datas")
                } catch (e: Exception) {
                    Timber.e(e)
                }

            }
        } else {
            viewModelScope.launch {
                try {
                    val datas = currencyRepo.getLocalData()
                    Timber.d("Data is: $datas")
                } catch (e: Exception) {
                    Timber.e(e)
                }

            }
        }
    }

    fun onCurrencyButtonClicked(field: CurrencyMode) {

    }

    private fun onCurrencySelected(field: CurrencyMode) {

    }
}