package com.example.currencyconverter.utils

import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import javax.inject.Inject

class DateTimeHelper @Inject constructor() {

    fun dateTimeBefore(datetime: String) : Boolean {
        val datetime = ISODateTimeFormat.dateTimeParser().parseDateTime(datetime)
        return datetime.isAfterNow
    }
}