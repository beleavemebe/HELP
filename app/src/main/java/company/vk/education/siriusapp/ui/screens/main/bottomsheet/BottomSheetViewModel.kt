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
            is MainScreenIntent.BottomSheetIntent.PickStartOnTheMap -> TODO()
            is MainScreenIntent.BottomSheetIntent.PickEndOnTheMap -> TODO()
            is MainScreenIntent.BottomSheetIntent.PickTripDate -> TODO()
            is MainScreenIntent.BottomSheetIntent.PickTripTime -> TODO()
            is MainScreenIntent.BottomSheetIntent.PickTaxiService -> TODO()
            is MainScreenIntent.BottomSheetIntent.PickTaxiVehicleClass -> TODO()
        }
    }
}