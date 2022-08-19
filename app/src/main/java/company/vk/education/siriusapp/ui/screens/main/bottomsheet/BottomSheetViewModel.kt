package company.vk.education.siriusapp.ui.screens.main.bottomsheet

import company.vk.education.siriusapp.ui.base.BaseViewModel
import company.vk.education.siriusapp.ui.screens.main.MainScreenIntent
import company.vk.education.siriusapp.ui.screens.main.MainViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BottomSheetViewModel @Inject constructor(
) : BaseViewModel<MainViewState.BottomSheetState, MainScreenIntent.BottomSheetIntent, Nothing>() {
    override val initialState: MainViewState.BottomSheetState
        get() = MainViewState.BottomSheetState.SearchTrips()

    override fun accept(intent: MainScreenIntent.BottomSheetIntent): Any {
        return when (intent) {
            is MainScreenIntent.BottomSheetIntent.PickStartOnTheMap -> {}
            is MainScreenIntent.BottomSheetIntent.PickEndOnTheMap -> {}
            is MainScreenIntent.BottomSheetIntent.PickTripDate -> {}
            is MainScreenIntent.BottomSheetIntent.PickTripTime -> {}
            is MainScreenIntent.BottomSheetIntent.PickTaxiService -> {}
            is MainScreenIntent.BottomSheetIntent.PickTaxiVehicleClass -> {}
        }
    }
}