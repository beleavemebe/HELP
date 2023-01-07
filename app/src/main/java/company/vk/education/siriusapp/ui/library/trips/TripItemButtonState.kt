package company.vk.education.siriusapp.ui.library.trips

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import company.vk.education.siriusapp.R
import company.vk.education.siriusapp.ui.theme.Blue
import company.vk.education.siriusapp.ui.theme.Green
import company.vk.education.siriusapp.ui.theme.Grey

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