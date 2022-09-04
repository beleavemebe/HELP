package company.vk.education.siriusapp.ui.screens.main.trip

import company.vk.education.siriusapp.R
import company.vk.education.siriusapp.core.Mapper
import company.vk.education.siriusapp.ui.library.trips.TripItemButtonState
import javax.inject.Inject

class TripItemButtonStateToStringResMapper @Inject constructor(
) : Mapper<TripItemButtonState, Int> {
    override fun map(arg: TripItemButtonState): Int {
        return when (arg) {
            TripItemButtonState.HOST -> R.string.show
            TripItemButtonState.JOIN -> R.string.join
            TripItemButtonState.BOOKED -> R.string.reserved
            TripItemButtonState.CONFLICT -> R.string.conflict_with_another_trip
            TripItemButtonState.INVISIBLE -> R.string.empty
        }
    }
}