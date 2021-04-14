package com.example.currencyconverter.core.datalayer

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize


data class Currency(
    var code: String? = null,
    var fraction: Double? = null
)