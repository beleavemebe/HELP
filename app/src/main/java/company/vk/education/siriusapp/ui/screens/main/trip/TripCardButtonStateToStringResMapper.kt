package company.vk.education.siriusapp.ui.screens.main.trip

import company.vk.education.siriusapp.R
import company.vk.education.siriusapp.core.Mapper
import javax.inject.Inject

class TripCardButtonStateToStringResMapper @Inject constructor(
) : Mapper<TripCardButtonState, Int> {
    override fun map(arg: TripCardButtonState): Int {
        return when (arg) {
            TripCardButtonState.HOST -> R.string.show
            TripCardButtonState.JOIN -> R.string.join
            TripCardButtonState.BOOKED -> R.string.reserved
            TripCardButtonState.CONFLICT -> R.string.conflict_with_another_trip
            TripCardButtonState.INVISIBLE -> R.string.empty
        }
    }
}