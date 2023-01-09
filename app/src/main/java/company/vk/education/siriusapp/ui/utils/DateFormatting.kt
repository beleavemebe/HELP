package company.vk.education.siriusapp.ui.utils

import android.text.format.DateFormat
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import company.vk.education.siriusapp.R
import java.util.*

fun Date?.formatOrEmpty(format: String): String {
    return if (this == null) "" else DateFormat.format(format, this).toString()
}

@Composable
fun formatDate(date: Date?): String {
    return if (date?.isToday == true) {
        stringResource(R.string.today)
    } else if (date?.isTomorrow == true) {
        stringResource(R.string.tomorrow)
    } else {
        date.formatOrEmpty("d MMM")
    }
}

fun formatTime(date: Date?): String {
    return date.formatOrEmpty("H:mm")
}