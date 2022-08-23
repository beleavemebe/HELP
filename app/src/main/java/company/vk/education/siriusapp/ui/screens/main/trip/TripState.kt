package company.vk.education.siriusapp.ui.screens.main.trip

import com.yandex.mapkit.geometry.Polyline
import company.vk.education.siriusapp.domain.model.Trip

data class TripState(
    val trip: Trip,
    val startAddress: String,
    val endAddress: String,
    val tripRoutePolyline: Polyline? = null
)