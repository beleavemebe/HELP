package com.help.android.ui.library.trips

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import com.help.android.R
import com.help.android.ui.theme.Blue
import com.help.android.ui.theme.Green
import com.help.android.ui.theme.Grey

enum class TripItemButtonState(
    val buttonColor: Color,
    @StringRes val buttonTextRes: Int,
) {
    HOST(Blue, R.string.show),
    JOIN(Blue, R.string.join),
    BOOKED(Green, R.string.reserved),
    CONFLICT(Grey, R.string.conflict_with_another_trip),
    INVISIBLE(White, R.string.empty)
}