package company.vk.education.siriusapp.ui.screens.main

import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import company.vk.education.siriusapp.ui.screens.main.bottomsheet.BottomSheetScreen
import company.vk.education.siriusapp.ui.screens.main.map.MapScreen
import kotlinx.coroutines.launch


@Composable
@OptIn(ExperimentalMaterialApi::class)
fun MainScreen(
    state: MainScreenState,
    mapView: MapView,
    userLocationLayer: UserLocationLayer,
    onDismissSheet: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetState)
    scope.launch {
        if (state.isBottomSheetExpanded) bottomSheetState.expand() else bottomSheetState.collapse()
    }

    BottomSheetScaffold(
        sheetContent = {
            BottomSheetScreen()
        },
        scaffoldState = scaffoldState,
        sheetPeekHeight = 170.dp
    ) {
        MapScreen(mapView, userLocationLayer)
    }

    if (state.profileToShow != null) {
        val modalBottomSheetState = rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
            confirmStateChange = { pendingState ->
                if (pendingState == ModalBottomSheetValue.Hidden) {
                    onDismissSheet()
                }
                false
            }
        )

        UserModalSheet(state.profileToShow, modalBottomSheetState)

        scope.launch {
            if (state.isShowingProfile) {
                modalBottomSheetState.show()
            } else {
                modalBottomSheetState.hide()
            }
        }
    }
}