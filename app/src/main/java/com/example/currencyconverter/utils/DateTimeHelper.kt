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
    fun dateTimeBefore(datetime: String) : Boolean {
        val date = SimpleDateFormat(WEB_API_TIMESTAMP_FORMAT, Locale.ENGLISH).parse(datetime)
        return date.before(Date())
    }
}