package company.vk.education.siriusapp.ui.screens.main

import company.vk.education.siriusapp.domain.model.TaxiService
import company.vk.education.siriusapp.domain.model.Trip
import company.vk.education.siriusapp.ui.base.BaseViewState
import java.util.*

sealed class MainViewState : BaseViewState {
    sealed class MapViewState : MainViewState() {
        data class Idle(
            val profilePic: String,
        ) : MapViewState()

        data class ChoosingAddress(
            val addressToChoose: AddressToChoose,
            val currentlyChosenAddress: String
        ) : MapViewState()
    }

    sealed class BottomSheetState : MainViewState() {
        data class SearchTrips(
            val startAddress: String,
            val endAddress: String,
            val date: Date,
            val time: HourAndMinute,
            val trips: List<Trip>
        ) : BottomSheetState()

        data class CreateTrip(
            val startAddress: String,
            val endAddress: String,
            val date: Date,
            val time: HourAndMinute,
            val freePlaces: Int,
            val taxiService: TaxiService,
            val verifyInstantly: Boolean
        )
    }
}