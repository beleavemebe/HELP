package company.vk.education.siriusapp.ui.screens.main.trip

import com.yandex.mapkit.geometry.Polyline
import company.vk.education.siriusapp.domain.model.Trip
import company.vk.education.siriusapp.ui.base.BaseViewState
import company.vk.education.siriusapp.ui.screens.trip.model.TripScreenTitle

data class TripState(
    val isLoading: Boolean = true,
    val trip: Trip? = null,
    val startAddress: String? = null,
    val endAddress: String? = null,
    val tripRoutePolyline: Polyline? = null,
    val title: TripScreenTitle = TripScreenTitle.TRIP_DETAILS,
    val showControls: Boolean = false,
) : BaseViewState