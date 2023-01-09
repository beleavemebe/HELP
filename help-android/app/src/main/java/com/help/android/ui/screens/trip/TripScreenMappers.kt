package com.help.android.ui.screens.trip

import com.help.android.core.Mapper
import com.help.android.ui.screens.trip.model.TripScreenTitle

interface TripScreenMappers {
    val tripScreenTitleMapper: Mapper<TripScreenTitle, Int>
}