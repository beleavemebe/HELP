package company.vk.education.siriusapp.ui.screens.home

import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.yandex.mapkit.mapview.MapView

@Composable
@OptIn(ExperimentalMaterialApi::class)
fun HomeScreen(mapView: MapView) {
    BottomSheetScaffold(
        sheetContent = {
            Text(text = "Sample text")
        }
    ) {
        AndroidView(factory = { mapView })
    }
}