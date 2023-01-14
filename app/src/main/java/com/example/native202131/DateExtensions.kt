package com.example.native202131

import android.os.Build
import android.text.format.DateFormat
import java.text.SimpleDateFormat
import java.util.*

fun String.fromIsoToDate(): Date {
    val isoFormat = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        "yyyy-MM-dd'T'HH:mm:ssX"
    } else {
        "yyyy-MM-dd'T'HH:mm:ssZ"
    }
    kotlin.runCatching {
        SimpleDateFormat(isoFormat, Locale.US).parse(this)
    }.onSuccess {
        return it ?: Date()
    }.onFailure {
        return Date(0)
    }
    return Date(0)
}

fun Date.toBestString(): String {
    kotlin.runCatching {
        val locale = Locale.getDefault()
        val pattern = DateFormat.getBestDateTimePattern(locale, "yyyyMMMdEEEHHmmss")
        SimpleDateFormat(pattern, locale).format(this@toBestString)
    }.onSuccess {
        return it
    }.onFailure {
        return it.message ?: ""
    }
    return ""
}
