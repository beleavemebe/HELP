package company.vk.education.siriusapp.ui.screens.main.bottomsheet

import company.vk.education.siriusapp.domain.model.TaxiService
import company.vk.education.siriusapp.domain.model.Trip
import java.util.*

data class BottomSheetScreenState(
    val startAddress: String = "",
    val endAddress: String = "",
    val isShowingDatePicker: Boolean = false,
    val isShowingTimePicker: Boolean = false,
    val date: Date? = null,
    val isSearchingTrips: Boolean = true,
    val areTripsLoading: Boolean = false,
    val freePlaces: Int? = null,
    val taxiService: TaxiService? = null,
    val verifyInstantly: Boolean = false,
    val trips: List<Trip>? = null
)