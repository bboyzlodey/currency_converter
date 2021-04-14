package com.example.currencyconverter.ui.convertion

import androidx.lifecycle.*
import com.example.currencyconverter.core.data.source.CacheSettings
import com.example.currencyconverter.core.data.source.local.DBCurrency
import com.example.currencyconverter.core.datalayer.CurrencyRepository
import com.example.currencyconverter.utils.DateTimeHelper
import com.example.currencyconverter.utils.DialogFactory
import com.example.currencyconverter.utils.swap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ConvertCurrencyViewModel @Inject constructor() : ViewModel(), LifecycleObserver {

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

    @OnLifecycleEvent(value = Lifecycle.Event.ON_START)
    fun onStart() {
        init()
    }

    private fun init() {
        val nextUpdate =
            cacheSettings.nextCurrencyRateUpdating?.let { dateTimeHelper.getLostTime(it) }
                ?: 100
        Timber.i("Next update exist with delay: $nextUpdate")
        viewModelScope.launch {
            try {
                availableCurrencies = currencyRepo.getLocalData()
                delay(nextUpdate)
                currencyRepo.updateLocalData()
            } catch (e: Exception) {
                Timber.e(e)
            }

        }
    }

    fun onSwapClicked() {
        Timber.i("onSwapClicked")
        selectedInputCurrency swap selectedOutputCurrency
        recalculate()
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

    fun onSourceInputTextChanged(text: String) {
        inputCurrencyValue = text
        recalculate()
    }

    private fun recalculate() {
        if (availableCurrencies == null) return
        val inputCurrency = availableCurrencies!!.find { it.code == selectedInputCurrency.value }
        val outputCurrency = availableCurrencies!!.find { it.code == selectedOutputCurrency.value }
        val inputValue = inputCurrencyValue.toFloatOrNull() ?: 0f
        if (inputCurrency == null || outputCurrency == null || inputValue == 0f) {
            outputCurrencyValue.value = "00.00"
        } else {
            outputCurrencyValue.value =
                ((inputValue / inputCurrency.fraction) * outputCurrency.fraction).toString()
        }
    }
}