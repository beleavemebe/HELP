package com.help.android.domain.model

import java.util.*

data class TripRoute(
    val startLocation: Location = Location.LOCATION_UNKNOWN,
    val endLocation: Location = Location.LOCATION_UNKNOWN,
    val date: Date = Date(),
)