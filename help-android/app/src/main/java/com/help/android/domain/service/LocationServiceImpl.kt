package com.help.android.domain.service

import com.help.android.domain.model.Location
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class LocationServiceImpl @Inject constructor() : LocationService {
    private val _currentLocation = MutableStateFlow(Location.LOCATION_UNKNOWN)
    override val currentLocation = _currentLocation.asStateFlow()

    override fun setCurrentLocation(location: Location) {
        _currentLocation.value = location
    }
}