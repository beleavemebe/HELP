package company.vk.education.siriusapp.domain.model

import java.util.*

data class TripRoute(
    val startLocation: Location,
    val endLocation: Location,
    val date: Date
)