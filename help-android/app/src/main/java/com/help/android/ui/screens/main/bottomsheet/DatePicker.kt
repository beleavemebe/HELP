package com.help.android.ui.screens.main.bottomsheet

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.help.android.ui.utils.calendar
import java.util.*

@Composable
fun DatePicker(
    prevDate: Date? = null,
    onDatePicked: (Date?) -> Unit
) {
    val cal = prevDate?.calendar() ?: Date().calendar()

    val pickedYear = cal.get(Calendar.YEAR)
    val pickedMonth = cal.get(Calendar.MONTH)
    val pickedDay = cal.get(Calendar.DAY_OF_MONTH)

    val onDateSetListener = { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val date = cal.run {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, dayOfMonth)
                time
            }
            onDatePicked(date)
        }
    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        onDateSetListener, pickedYear, pickedMonth, pickedDay
    )

    datePickerDialog.setOnCancelListener {
        onDatePicked(prevDate)
    }

    datePickerDialog.show()
}
