package company.vk.education.siriusapp.ui.screens.main.trip

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import company.vk.education.siriusapp.core.Mapper
import company.vk.education.siriusapp.ui.theme.Blue
import company.vk.education.siriusapp.ui.theme.Green
import company.vk.education.siriusapp.ui.theme.Grey
import javax.inject.Inject

class TripCardButtonStateToColorMapper @Inject constructor(
) : Mapper<TripCardButtonState, Color> {
    override fun map(arg: TripCardButtonState): Color {
        return when (arg) {
            TripCardButtonState.HOST -> Blue
            TripCardButtonState.JOIN -> Blue
            TripCardButtonState.BOOKED -> Green
            TripCardButtonState.CONFLICT -> Grey
            TripCardButtonState.INVISIBLE -> White
        }
    }
}