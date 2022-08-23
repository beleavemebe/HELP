package company.vk.education.siriusapp.ui.screens.main

import company.vk.education.siriusapp.domain.model.Location
import company.vk.education.siriusapp.domain.model.TaxiService
import company.vk.education.siriusapp.domain.model.TaxiVehicleClass
import company.vk.education.siriusapp.ui.base.BaseViewIntent
import company.vk.education.siriusapp.ui.screens.main.bottomsheet.TaxiPreference
import kotlinx.coroutines.CoroutineScope
import java.util.*

sealed class MainScreenIntent : BaseViewIntent {
    object DismissUserModalSheet : MainScreenIntent()

    sealed class MapIntent : MainScreenIntent() {
        data class UpdatePickedLocation(val location: Location) : MapIntent()
        data class AddressChosen(val addressToChoose: AddressToChoose, val addressLocation: Location) : MapIntent()
        data class UserLocationAcquired(val location: Location) : MainScreenIntent()
        object ShowProfile : MapIntent()
    }

    sealed class BottomSheetIntent : MainScreenIntent() {
        object PickStartOnTheMap : BottomSheetIntent()
        object PickEndOnTheMap : BottomSheetIntent()
        object PickTripDate : BottomSheetIntent()
        object PickTripTime : BottomSheetIntent()
        data class TripDatePicked(val date: Date?) : BottomSheetIntent()
        data class TripTimePicked(val hourAndMinute: HourAndMinute) : BottomSheetIntent()
        object CreateTrip : MainScreenIntent()
        data class PickTaxiPreference(val preference: TaxiPreference) : BottomSheetIntent()
        data class DismissTaxiPreferenceMenu(val preference: TaxiPreference) : BottomSheetIntent()
        data class TaxiServicePicked(val taxiService: TaxiService) : BottomSheetIntent()
        data class TaxiVehicleClassPicked(val taxiVehicleClass: TaxiVehicleClass) : BottomSheetIntent()
        data class SetFreePlacesAmount(val freePlaces: Int) : MainScreenIntent()
    }
}