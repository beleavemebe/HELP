package com.help.android.ui.screens.main.bottomsheet

import android.app.TimePickerDialog
import android.widget.TimePicker
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.help.android.core.HourAndMinute
import com.help.android.ui.utils.calendar
import java.util.*

@Composable
fun TimePicker(
    pickedDate: Date,
    onTimePicked: (HourAndMinute) -> Unit
) {
    val cal = pickedDate.calendar()

    val onTimeSetListener = { _: TimePicker, hour: Int, minute: Int ->
        onTimePicked(HourAndMinute(hour to minute))
    }

    val pickedHour = cal.get(Calendar.HOUR_OF_DAY)
    val pickedMinute = cal.get(Calendar.MINUTE)
    val is24HourView = true

    val datePickerDialog = TimePickerDialog(
        LocalContext.current,
        onTimeSetListener,
        pickedHour,
        pickedMinute,
        is24HourView
    )

    datePickerDialog.setOnCancelListener {
        onTimePicked(HourAndMinute(pickedHour to pickedMinute))
    }

    datePickerDialog.show()
}
