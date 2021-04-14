package com.example.currencyconverter.utils

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("show")
fun show(view: View, isShow: Boolean) {
    view.show(isShow)
}
