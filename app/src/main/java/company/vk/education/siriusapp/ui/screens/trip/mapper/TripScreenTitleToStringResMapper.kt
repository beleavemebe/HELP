package company.vk.education.siriusapp.ui.screens.trip.mapper

import company.vk.education.siriusapp.R
import company.vk.education.siriusapp.core.Mapper
import company.vk.education.siriusapp.ui.screens.trip.model.TripScreenTitle
import javax.inject.Inject

class TripScreenTitleToStringResMapper @Inject constructor() : Mapper<TripScreenTitle, Int> {
    override fun map(arg: TripScreenTitle): Int {
        return when (arg) {
            TripScreenTitle.TRIP_DETAILS -> R.string.trip_details
            TripScreenTitle.TRIP_CREATED -> R.string.trip_created
            TripScreenTitle.CURRENT_TRIP -> R.string.current_trip
            TripScreenTitle.TRIP_FINISHED -> R.string.trip_finished
        }
    }
}