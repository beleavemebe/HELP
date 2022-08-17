package company.vk.education.siriusapp.ui.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.viewinterop.AndroidView
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.mapview.MapView
import company.vk.education.siriusapp.domain.model.Location
import company.vk.education.siriusapp.ui.screens.home.HomeScreen
import company.vk.education.siriusapp.ui.screens.home.HomeViewModel
import company.vk.education.siriusapp.ui.screens.home.MainScreen
import company.vk.education.siriusapp.ui.screens.home.Trips
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var mapView: MapView

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        MapKitFactory.initialize(this)
        super.onCreate(savedInstanceState)
        mapView = MapView(this)
        setContent {
            MainScreen(
                Content = { Map() },
                SheetContent = { BottomSheet() }
            )
        }
        homeViewModel.consume("Юрга, пр-кт победы 30а")
        homeViewModel.consume(Location(37.611446, 55.760256))
    }

    @Composable
    private fun Map() {
        AndroidView(factory = { mapView })
    }

    @Composable
    private fun BottomSheet() {
//        Text(text = state.toString())
//        Trips(state.value.trips)
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }
}