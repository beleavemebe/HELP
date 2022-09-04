package company.vk.education.siriusapp.ui.library.trips

import company.vk.education.siriusapp.domain.model.Trip

data class TripCard(
    val trip: Trip,
    val dist: Int,
    val isCurrentTrip: Boolean,
    val tripItemButtonState: TripItemButtonState,
)