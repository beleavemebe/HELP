package company.vk.education.siriusapp.ui.screens.main

import company.vk.education.siriusapp.domain.model.Location
import company.vk.education.siriusapp.ui.base.BaseViewIntent

sealed class MainScreenIntent : BaseViewIntent {
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
        object PickTaxiService : BottomSheetIntent()
        object PickTaxiVehicleClass : BottomSheetIntent()
    }
}