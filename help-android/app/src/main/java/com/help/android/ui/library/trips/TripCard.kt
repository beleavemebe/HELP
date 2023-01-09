package com.help.android.ui.library.trips

import com.help.android.domain.model.Trip

data class TripCard(
    val trip: Trip,
    val dist: Int,
    val isCurrentTrip: Boolean,
    val tripItemButtonState: TripItemButtonState,
)