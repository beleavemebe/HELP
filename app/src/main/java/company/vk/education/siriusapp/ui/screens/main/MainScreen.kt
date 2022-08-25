package company.vk.education.siriusapp.ui.screens.main

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import company.vk.education.siriusapp.ui.screens.main.bottomsheet.BottomSheetScreen
import company.vk.education.siriusapp.ui.screens.main.map.MapScreen
import company.vk.education.siriusapp.ui.screens.main.trip.TripModalSheet
import company.vk.education.siriusapp.ui.theme.Spacing32dp
import kotlinx.coroutines.launch


@Composable
@OptIn(ExperimentalMaterialApi::class)
fun MainScreen(
    state: MainScreenState,
    mapView: MapView,
    userLocationLayer: UserLocationLayer,
    onDismissUserSheet: () -> Unit,
    onDismissTripSheet: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetState)
    val sheetPeekHeight = if (state.mapState.isChoosingAddress) 0.dp else 186.dp
    var previousState by remember { mutableStateOf(state.isBottomSheetExpanded) }
    scope.launch {
        if (state.isBottomSheetExpanded == previousState) return@launch

        if (state.isBottomSheetExpanded) {
            bottomSheetState.expand()
        } else {
            bottomSheetState.collapse()
        }

        previousState = state.isBottomSheetExpanded
    }

    BottomSheetScaffold(
        sheetContent = {
            BottomSheetScreen()
        },
        scaffoldState = scaffoldState,
        sheetPeekHeight = sheetPeekHeight,
        sheetShape = RoundedCornerShape(Spacing32dp)
    ) {
        MapScreen(mapView, userLocationLayer)
    }

    var previousUserSheetState by remember { mutableStateOf(state.isShowingUser) }
    if (state.userState?.user != null) {
        val userSheetState = rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
            confirmStateChange = { pendingState ->
                if (pendingState == ModalBottomSheetValue.Hidden) {
                    onDismissUserSheet()
                }
                false
            }
        )

        UserModalSheet(state.userState, userSheetState)

        scope.launch {
            if (state.isShowingUser == previousUserSheetState) return@launch

            if (state.isShowingUser) {
                userSheetState.show()
            } else {
                userSheetState.hide()
            }

            previousUserSheetState = state.isShowingUser
        }
    }

    var previousTripSheetState by remember { mutableStateOf(state.isShowingUser) }
    if (state.tripState != null) {
        val tripSheetState = rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
            confirmStateChange = { pendingState ->
                if (pendingState == ModalBottomSheetValue.Hidden) {
                    onDismissTripSheet()
                }
                false
            }
        )

        TripModalSheet(state.tripState, tripSheetState)

        scope.launch {
            tripSheetState.show()
        }
    }
}
