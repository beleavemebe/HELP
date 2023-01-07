package company.vk.education.siriusapp.ui.screens.main.bottomsheet

import company.vk.education.siriusapp.domain.model.Location
import company.vk.education.siriusapp.domain.model.TaxiService
import company.vk.education.siriusapp.domain.model.TaxiVehicleClass
import company.vk.education.siriusapp.ui.library.trips.TripCard
import java.util.*

data class BottomSheetScreenState(
    val startAddress: String = "",
    val endAddress: String = "",
    val startLocation: Location = Location.LOCATION_UNKNOWN,
    val endLocation: Location = Location.LOCATION_UNKNOWN,
    val isShowingDatePicker: Boolean = false,
    val isShowingTimePicker: Boolean = false,
    val date: Date? = null,
    val isSearchingTrips: Boolean = true,
    val areTripsLoading: Boolean = false,
    val freePlaces: Int? = null,
    val isShowingPickTaxiServiceMenu: Boolean = false,
    val isShowingPickTaxiVehicleClassMenu: Boolean = false,
    val taxiService: TaxiService? = null,
    val taxiVehicleClass: TaxiVehicleClass? = null,
    val trips: List<TripCard>? = null,
    val startAddressErrorMsg: String? = null,
    val endAddressErrorMsg: String? = null,
    val dateErrorMsg: String? = null,
    val freePlacesErrorMsg: String? = null,
    val taxiServiceErrorMsg: String? = null,
    val taxiVehicleClassErrorMsg: String? = null,
)