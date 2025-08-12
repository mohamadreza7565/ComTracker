package com.app.comtracker.domain.utils

import android.os.Build
import com.app.comtracker.domain.model.TrackerHistoryType
import saman.zamani.persiandate.PersianDate
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

internal fun getTrackerHistoryTypeByKey(key: Int): TrackerHistoryType {
    return try {
        TrackerHistoryType.entries.find { it.key == key } ?: TrackerHistoryType.OTHER
    } catch (_: IllegalArgumentException) {
        TrackerHistoryType.OTHER
    }
}

internal fun getPersianDate(dateFormated: String): String {

    var date: Date

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val localDateTime = LocalDateTime.parse(dateFormated, formatter)
        date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant())
    } else {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        date = sdf.parse(dateFormated)

    }

    val persianDate = PersianDate(date)
    val persianDateFormatted =
        "${persianDate.shYear}/${persianDate.shMonth}/${persianDate.shDay} - ${persianDate.hour}:${persianDate.minute}"
    return persianDateFormatted
}