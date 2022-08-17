package company.vk.education.siriusapp.ui.screens.main

import company.vk.education.siriusapp.ui.base.BaseViewIntent

sealed class MainScreenIntent : BaseViewIntent {
    sealed class MapIntent : MainScreenIntent() {
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