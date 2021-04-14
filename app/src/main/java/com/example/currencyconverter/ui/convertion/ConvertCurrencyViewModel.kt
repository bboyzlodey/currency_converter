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
import kotlinx.coroutines.flow.collect
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
    var selectedInputCurrency = MutableLiveData<String>("USD")
    var selectedOutputCurrency = MutableLiveData<String>("RUB")
    var outputCurrencyValue = MutableLiveData<String>("")
    private var inputCurrencyValue = "1.0"
    private val moneyFormat = "%.2f"

    @OnLifecycleEvent(value = Lifecycle.Event.ON_START)
    fun onStart() {
        recalculate()
        getData()
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
                moneyFormat.format(((inputValue / inputCurrency.fraction) * outputCurrency.fraction))
                    .replace(',', '.')
        }
    }

    private fun getData() {
        viewModelScope.launch {
            currencyRepo.getLocalData()
                .collect {
                    processCurrencyRateUpdated(rates = it)
                    createUpdatingCoroutine()
                }
        }
    }

    private fun processCurrencyRateUpdated(rates: List<DBCurrency>) {
        Timber.i("processCurrencyRateUpdated")
        Timber.d("new currencies: $rates")
        availableCurrencies = rates
        recalculate()
    }

    private fun createUpdatingCoroutine() {
        val nextUpdate =
            cacheSettings.nextCurrencyRateUpdating?.let { dateTimeHelper.getLostTime(it) } ?: 100
        viewModelScope.launch {
            try {
                delay(nextUpdate)
                Timber.i("delay start")
                delay(nextUpdate)
                Timber.i("delay ended")
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
}