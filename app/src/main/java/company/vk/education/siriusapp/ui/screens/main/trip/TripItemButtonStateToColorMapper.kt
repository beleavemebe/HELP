package company.vk.education.siriusapp.ui.screens.main.trip

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import company.vk.education.siriusapp.core.Mapper
import company.vk.education.siriusapp.ui.library.trips.TripItemButtonState
import company.vk.education.siriusapp.ui.theme.Blue
import company.vk.education.siriusapp.ui.theme.Green
import company.vk.education.siriusapp.ui.theme.Grey
import javax.inject.Inject

class TripItemButtonStateToColorMapper @Inject constructor(
) : Mapper<TripItemButtonState, Color> {
    override fun map(arg: TripItemButtonState): Color {
        return when (arg) {
            TripItemButtonState.HOST -> Blue
            TripItemButtonState.JOIN -> Blue
            TripItemButtonState.BOOKED -> Green
            TripItemButtonState.CONFLICT -> Grey
            TripItemButtonState.INVISIBLE -> White
        }
    }
}