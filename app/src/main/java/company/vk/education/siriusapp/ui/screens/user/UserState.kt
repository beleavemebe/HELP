package company.vk.education.siriusapp.ui.screens.user

import company.vk.education.siriusapp.domain.model.Trip
import company.vk.education.siriusapp.domain.model.User
import company.vk.education.siriusapp.ui.base.BaseViewState

data class UserState(
    val isLoading: Boolean = true,
    val user: User? = null,
    val currentTrip: Trip? = null,
    val scheduledTrips: List<Trip> = emptyList(),
    val previousTrips: List<Trip> = emptyList(),
) : BaseViewState
