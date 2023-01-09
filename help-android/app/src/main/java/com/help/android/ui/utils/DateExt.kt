package com.help.android.ui.utils

import android.text.format.DateUtils
import com.help.android.core.HourAndMinute
import java.util.*

fun Date.calendar(): Calendar {
    val date = this
    return Calendar.getInstance().apply {
        time = date
    }
}

fun Date.setHourAndMinute(
    hourAndMinute: HourAndMinute
): Date = calendar().run {
    set(Calendar.HOUR_OF_DAY, hourAndMinute.hour)
    set(Calendar.MINUTE, hourAndMinute.minute)
    time
}

fun Date.getHourAndMinute(): HourAndMinute = calendar().run {
    HourAndMinute(get(Calendar.HOUR_OF_DAY) to get(Calendar.MINUTE))
}

const val MINUTE_MS = 60 * 1000L
const val HOUR_MS = 60 * MINUTE_MS
const val DAY_MS = 24 * HOUR_MS

val Date?.isToday get() =
    this?.let { DateUtils.isToday(it.time) } ?: false

val Date?.isTomorrow get() =
    this?.let { Date(it.time - DAY_MS).isToday } ?: false
