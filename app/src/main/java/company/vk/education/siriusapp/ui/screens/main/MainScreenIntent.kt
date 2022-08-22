package company.vk.education.siriusapp.ui.screens.main

import company.vk.education.siriusapp.domain.model.Location
import company.vk.education.siriusapp.ui.base.BaseViewIntent
import kotlinx.coroutines.CoroutineScope
import java.util.*

sealed class MainScreenIntent : BaseViewIntent {
    object DismissUserModalSheet : MainScreenIntent()

    sealed class MapIntent : MainScreenIntent() {
        data class UpdatePickedLocation(val location: Location) : MapIntent()
        data class AddressChosen(val addressToChoose: AddressToChoose, val addressLocation: Location) : MapIntent()
        object ShowProfile : MapIntent()
    }

    sealed class BottomSheetIntent : MainScreenIntent() {
        object PickStartOnTheMap : BottomSheetIntent()
        object PickEndOnTheMap : BottomSheetIntent()
        object PickTripDate : BottomSheetIntent()
        object PickTripTime : BottomSheetIntent()
        data class TripDatePicked(val date: Date?) : BottomSheetIntent()
        data class TripTimePicked(val hourAndMinute: HourAndMinute) : BottomSheetIntent()
        object PickTaxiService : BottomSheetIntent()
        object PickTaxiVehicleClass : BottomSheetIntent()
    }
}