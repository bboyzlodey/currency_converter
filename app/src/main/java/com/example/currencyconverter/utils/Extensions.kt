package com.example.currencyconverter.utils

import android.view.View
import android.view.View.*

fun View.show(isShow: Boolean) {
    visibility = if (isShow) VISIBLE else GONE
}