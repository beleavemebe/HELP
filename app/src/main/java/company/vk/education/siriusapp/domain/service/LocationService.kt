package company.vk.education.siriusapp.domain.service

import company.vk.education.siriusapp.domain.model.Location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface LocationService {
    val currentLocation: StateFlow<Location>

    fun setCurrentLocation(location: Location)
}