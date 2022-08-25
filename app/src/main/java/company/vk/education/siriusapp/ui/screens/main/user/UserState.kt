package company.vk.education.siriusapp.ui.screens.main.user

import company.vk.education.siriusapp.domain.model.Trip
import company.vk.education.siriusapp.domain.model.User

data class UserState(
    val user: User,
    val currentTrip: Trip? = null,
    val scheduledTrips: List<Trip> = emptyList(),
    val previousTrips: List<Trip> = emptyList(),
)
