package com.help.android.domain.service

import com.help.android.domain.model.Location
import kotlinx.coroutines.flow.StateFlow

interface LocationService {
    val currentLocation: StateFlow<Location>

    fun setCurrentLocation(location: Location)
}