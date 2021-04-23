package com.example.currencyconverter.utils

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan

//extension функция для edit text
object TextHelper {

    // вместо { =
    fun applyColor(text: String, coloredString: String, color: Int): CharSequence =
        SpannableString(text).apply {
            val lastIndex = text.lastIndexOf(coloredString)
            setSpan(
                ForegroundColorSpan(color),
                lastIndex,
                lastIndex + coloredString.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
}