package com.help.android.ui.screens.trip.mapper

import com.help.android.R
import com.help.android.core.Mapper
import com.help.android.ui.screens.trip.model.TripScreenTitle
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