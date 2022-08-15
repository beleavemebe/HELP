package company.vk.education.siriusapp.domain.service

import company.vk.education.siriusapp.domain.model.Location
import kotlinx.coroutines.flow.Flow

interface LocationService {
    val currentLocation: Flow<Location>
}