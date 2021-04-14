package com.example.currencyconverter.ui.convertion

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.core.data.source.CacheSettings
import com.example.currencyconverter.core.data.source.local.DBCurrency
import com.example.currencyconverter.core.datalayer.CurrencyRepository
import com.example.currencyconverter.utils.DateTimeHelper
import com.example.currencyconverter.utils.DialogFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ConvertCurrencyViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var dateTimeHelper: DateTimeHelper

    @Inject
    lateinit var currencyRepo: CurrencyRepository

    @Inject
    lateinit var cacheSettings: CacheSettings

    val dialog = MutableLiveData<DialogFactory.DialogData>()

    private var availableCurrencies: List<DBCurrency>? = null
    var selectedInputCurrency = MutableLiveData<String>()
    var selectedOutputCurrency = MutableLiveData<String>()
    var outputCurrencyValue = MutableLiveData<String>("")
    private var inputCurrencyValue = "0.0"

    fun onSwapClicked() {
        Timber.i("onSwipeClicked!")
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
        val currencyList = availableCurrencies?.map { it.code } ?: emptyList()
        dialog.value = when (field) {
            CurrencyMode.INPUT -> {
                DialogFactory.DialogData(
                    {},
                    "Cancel",
                    "Apply",
                    currencyList,
                    currencyList.indexOf(selectedInputCurrency.value),
                    {
                        selectedInputCurrency.value = currencyList[it]
                        recalculate()
                    }
                )
            }
            CurrencyMode.OUTPUT -> {
                DialogFactory.DialogData(
                    {},
                    "Cancel",
                    "Apply",
                    currencyList,
                    currencyList.indexOf(selectedOutputCurrency.value),
                    {
                        selectedOutputCurrency.value = currencyList[it]
                        recalculate()
                    }
                )
            }
        }
    }

    private fun recalculate() {
        if (availableCurrencies == null) return
        val inputCurrency =  availableCurrencies!!.find { it.code == selectedInputCurrency.value }
        val outputCurrency = availableCurrencies!!.find { it.code == selectedOutputCurrency.value }
        val inputValue = inputCurrencyValue.toFloatOrNull() ?: 0f
        if (inputCurrency == null || outputCurrency == null || inputValue == 0f) {
            outputCurrencyValue.value = "00.00"
        } else {
            outputCurrencyValue.value = ((inputValue / inputCurrency.fraction) * outputCurrency.fraction).toString()
        }
    }

    private fun onCurrencySelected(field: CurrencyMode) {

    }
}