package com.help.android.ui.screens.main.bottomsheet

import com.help.android.domain.model.Location
import com.help.android.domain.model.TaxiService
import com.help.android.domain.model.TaxiVehicleClass
import com.help.android.ui.library.trips.TripCard
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