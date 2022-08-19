package company.vk.education.siriusapp.ui.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.mapview.MapView
import company.vk.education.siriusapp.domain.service.AuthService
import company.vk.education.siriusapp.ui.screens.home.MainScreen
import company.vk.education.siriusapp.ui.screens.main.bottomsheet.BottomSheetScreen
import company.vk.education.siriusapp.ui.screens.main.map.MapScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var mapView: MapView

    @Inject
    lateinit var authService: AuthService

    override fun onCreate(savedInstanceState: Bundle?) {
        MapKitFactory.initialize(this)
        super.onCreate(savedInstanceState)
        mapView = MapView(this)
        setContent {
            MainScreen(
                Content = { MapScreen(mapView) },
                SheetContent = { BottomSheetScreen() }
            )
        }
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