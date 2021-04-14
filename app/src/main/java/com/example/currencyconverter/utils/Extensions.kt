package com.example.currencyconverter.utils

import android.view.View
import android.view.View.*
import androidx.lifecycle.MutableLiveData

fun View.show(isShow: Boolean) {
    visibility = if (isShow) VISIBLE else GONE
}

infix  fun <T> MutableLiveData<T>.swap(with: MutableLiveData<T>) {
    (this.value to with.value).apply {
        value = second
        with.value = first
    }
}