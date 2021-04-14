package com.example.currencyconverter.ui.convertion

import android.content.Context
import androidx.lifecycle.*
import com.example.currencyconverter.R
import com.example.currencyconverter.data.CacheSettings
import com.example.currencyconverter.data.local.DBCurrency
import com.example.currencyconverter.data.CurrencyRepository
import com.example.currencyconverter.utils.DateTimeHelper
import com.example.currencyconverter.utils.DialogFactory
import com.example.currencyconverter.utils.swap
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ConvertCurrencyViewModel @Inject constructor() : ViewModel(), LifecycleObserver {

    @ApplicationContext
    @Inject
    lateinit var context: Context

    @Inject
    lateinit var dateTimeHelper: DateTimeHelper

    @Inject
    lateinit var currencyRepo: CurrencyRepository

    @Inject
    lateinit var cacheSettings: CacheSettings


    private var availableCurrencies: List<DBCurrency>? = null
    private var inputCurrencyValue = "1.0"

    val dialog = MutableLiveData<DialogFactory.DialogData>()
    var selectedInputCurrency = MutableLiveData<String>("USD")
    var selectedOutputCurrency = MutableLiveData<String>("RUB")
    var outputCurrencyValue = MutableLiveData<String>("")
    var isLoading = MutableLiveData<Boolean>()

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
            val newOutputCurrencyValue =
                ((inputValue / inputCurrency.fraction) * outputCurrency.fraction)
            outputCurrencyValue.value =
                context.getString(R.string.currency_format, newOutputCurrencyValue)
                    .replace(',', '.')
        }
    }

    private fun getData() {
        viewModelScope.launch {
            currencyRepo.getCurrencyRates()
                .onStart { isLoading.value = true }
                .onCompletion { isLoading.value = false }
                .collect {
                    isLoading.value = false
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
                currencyRepo.fetchCurrencyRates()
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
            CurrencyMode.SOURCE -> {
                DialogFactory.DialogData(
                    context.getString(R.string.choose_currency_dialog_title),
                    {},
                    context.getString(R.string.ok),
                    context.getString(R.string.cancel),
                    currencyList,
                    currencyList.indexOf(selectedInputCurrency.value),
                    {
                        selectedInputCurrency.value = currencyList[it]
                        recalculate()
                    }
                )
            }
            CurrencyMode.TARGET -> {
                DialogFactory.DialogData(
                    context.getString(R.string.choose_currency_dialog_title),
                    {},
                    context.getString(R.string.ok),
                    context.getString(R.string.cancel),
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