package com.example.currencyconverter.ui.convertion

import android.content.Context
import androidx.lifecycle.*
import com.example.currencyconverter.R
import com.example.currencyconverter.data.CacheSettings
import com.example.currencyconverter.data.CurrencyRepository
import com.example.currencyconverter.data.local.DBCurrency
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


    private var availableCurrencies: List<DBCurrency> = emptyList()
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
        val inputCurrency = availableCurrencies.find { it.code == selectedInputCurrency.value }
        val outputCurrency = availableCurrencies.find { it.code == selectedOutputCurrency.value }
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
        availableCurrencies = rates
        recalculate()
    }

    private fun createUpdatingCoroutine() {
        val nextUpdate =
            cacheSettings.nextCurrencyRateUpdating?.let { dateTimeHelper.getLostTime(it) } ?: 100
        viewModelScope.launch {
            try {
                delay(nextUpdate)
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
        fun getMutableData(): MutableLiveData<String> {
            return when (field) {
                CurrencyMode.SOURCE -> selectedInputCurrency
                CurrencyMode.TARGET -> selectedOutputCurrency
            }
        }

        val chooseCurrencyDialogData = getDefaultDialogData()
        val mutableField = getMutableData()

        chooseCurrencyDialogData.run {
            selectedItem = listData.indexOf(mutableField.value)
            itemSelectedListener = {
                mutableField.value = listData[it]
                recalculate()
            }
        }

        dialog.value = chooseCurrencyDialogData
    }

    private fun getDefaultDialogData(): DialogFactory.DialogData {
        val currencyList = availableCurrencies.map { it.code }
        return DialogFactory.DialogData(
            title = context.getString(R.string.choose_currency_dialog_title),
            neutralClicked = {},
            positiveButtonTitle = context.getString(R.string.ok),
            neutralButtonTitle = context.getString(R.string.cancel),
            listData = currencyList,
            selectedItem = 0,
            itemSelectedListener = {}
        )
    }

    fun onSourceInputTextChanged(text: String) {
        inputCurrencyValue = text
        recalculate()
    }
}