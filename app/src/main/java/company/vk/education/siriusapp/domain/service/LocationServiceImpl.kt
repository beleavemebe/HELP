package company.vk.education.siriusapp.domain.service

import company.vk.education.siriusapp.domain.model.Location
import kotlinx.coroutines.flow.Flow
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