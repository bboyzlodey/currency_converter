package com.example.currencyconverter.utils

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan

object TextHelper {

    fun applyColor(text: String, coloredString: String, color: Int): CharSequence {
        return SpannableString(text).apply {
            val lastIndex = text.lastIndexOf(coloredString)
            setSpan(
                ForegroundColorSpan(color),
                lastIndex,
                lastIndex + coloredString.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }
}