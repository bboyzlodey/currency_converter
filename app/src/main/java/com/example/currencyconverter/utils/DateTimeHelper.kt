package com.example.currencyconverter.utils

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

class DateTimeHelper @Inject constructor() {

    companion object {
        const val WEB_API_TIMESTAMP_FORMAT = "E, dd MMM yyyy HH:mm:ss Z"
    }

    private val now: Date
            get() = Date()

    private fun getDateTime(datetime: String, format: String = WEB_API_TIMESTAMP_FORMAT) : Date{
        val date = SimpleDateFormat(format, Locale.ENGLISH).parse(datetime)
        return date
    }

    fun getLostTime(datetime: String) : Long {
        val lostTime = getDateTime(datetime).time - now.time
        return if (lostTime > 0) lostTime else 0
    }
}