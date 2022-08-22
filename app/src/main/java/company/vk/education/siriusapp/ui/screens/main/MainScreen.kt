package company.vk.education.siriusapp.ui.screens.main

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
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
}