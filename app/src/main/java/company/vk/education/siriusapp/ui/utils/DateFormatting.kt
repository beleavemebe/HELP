package company.vk.education.siriusapp.ui.utils

import android.text.format.DateFormat
import java.util.*

fun Date?.formatOrEmpty(format: String): String {
    return if (this == null) "" else DateFormat.format(format, this).toString()
}
