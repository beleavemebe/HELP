package com.help.android.ui.screens.main

import androidx.compose.runtime.compositionLocalOf
import com.help.android.core.HourAndMinute
import com.help.android.domain.model.*
import com.help.android.ui.base.BaseViewIntent
import com.help.android.ui.base.IntentConsumer
import com.help.android.ui.screens.main.bottomsheet.TaxiPreference
import java.util.*

sealed class MainScreenIntent : BaseViewIntent {
    object CollapseBottomSheet : MainScreenIntent()
    object ExpandBottomSheet : MainScreenIntent()

    data class ShowUser(val user: User): MainScreenIntent()

    sealed class MapIntent : MainScreenIntent() {
        data class UpdatePickedLocation(val location: Location) : MapIntent()
        data class AddressChosen(val addressToChoose: AddressToChoose, val addressLocation: Location) : MapIntent()
        data class UserLocationAcquired(val location: Location) : MainScreenIntent()
        object MoveToUserLocation : MapIntent()
        object ShowMyProfile : MapIntent()
        object InvalidateChosenAddress : MainScreenIntent()
        object LocationPermissionGranted : MainScreenIntent()
    }

    sealed class BottomSheetIntent : MainScreenIntent() {
        object PickStartOnTheMap : BottomSheetIntent()
        object PickEndOnTheMap : BottomSheetIntent()
        object PickTripDate : BottomSheetIntent()
        object PickTripTime : BottomSheetIntent()
        data class TripDatePicked(val date: Date?) : BottomSheetIntent()
        data class TripTimePicked(val hourAndMinute: HourAndMinute) : BottomSheetIntent()
        object CreateTrip : BottomSheetIntent()
        data class PickTaxiPreference(val preference: TaxiPreference) : BottomSheetIntent()
        data class DismissTaxiPreferenceMenu(val preference: TaxiPreference) : BottomSheetIntent()
        data class TaxiServicePicked(val taxiService: TaxiService) : BottomSheetIntent()
        data class TaxiVehicleClassPicked(val taxiVehicleClass: TaxiVehicleClass) : BottomSheetIntent()
        data class SetFreePlacesAmount(val freePlaces: Int) : BottomSheetIntent()
        object PublishTrip : BottomSheetIntent()
        object CancelCreatingTrip : BottomSheetIntent()
        data class ShowTripDetails(val trip: Trip) : MainScreenIntent()
        data class JoinTrip(val trip: Trip) : BottomSheetIntent()
    }
}

val LocalMainScreenIntentConsumer = compositionLocalOf {
    IntentConsumer<MainScreenIntent> {}
}