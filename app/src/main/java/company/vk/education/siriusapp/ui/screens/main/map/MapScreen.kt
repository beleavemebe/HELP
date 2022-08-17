package company.vk.education.siriusapp.ui.screens.main.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yandex.mapkit.mapview.MapView
import company.vk.education.siriusapp.ui.screens.main.MainViewState

@Composable
fun MapScreen(
    mapView: MapView,
    viewModel: MapViewModel = viewModel()
) = Map(mapView, viewModel.viewState.collectAsState())

@Composable
fun Map(mapView: MapView, state: State<MainViewState.MapViewState>) {
    AndroidView(factory = { mapView })
}