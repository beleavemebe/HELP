package company.vk.education.siriusapp.ui.utils

import company.vk.education.siriusapp.ui.screens.main.HourAndMinute
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
