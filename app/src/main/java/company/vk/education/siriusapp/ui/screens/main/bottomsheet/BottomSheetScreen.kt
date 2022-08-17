package company.vk.education.siriusapp.ui.screens.main.bottomsheet

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import company.vk.education.siriusapp.ui.screens.main.MainViewState

@Composable
fun BottomSheetScreen(
    viewModel: BottomSheetViewModel = viewModel()
) = BottomSheet(viewModel.viewState.collectAsState())

@Composable
fun BottomSheet(
    state: State<MainViewState.BottomSheetState>
) {
    when (state.value) {
        is MainViewState.BottomSheetState.SearchTrips -> TODO()
        is MainViewState.BottomSheetState.CreateTrip -> TODO()
    }
}
