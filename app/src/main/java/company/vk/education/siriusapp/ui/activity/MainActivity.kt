package company.vk.education.siriusapp.ui.activity

import android.content.pm.PackageManager
import android.graphics.PointF
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.RotationType
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.image.ImageProvider
import company.vk.education.siriusapp.R
import company.vk.education.siriusapp.domain.model.Location
import company.vk.education.siriusapp.domain.service.AuthService
import company.vk.education.siriusapp.ui.screens.main.AddressToChoose
import company.vk.education.siriusapp.ui.screens.main.MainScreen
import company.vk.education.siriusapp.ui.screens.main.MainScreenIntent
import company.vk.education.siriusapp.ui.screens.main.MainViewModel
import company.vk.education.siriusapp.ui.theme.Blue
import company.vk.education.siriusapp.ui.utils.moveToUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()

    private lateinit var mapView: MapView
    private lateinit var userLocationLayer: UserLocationLayer

    private val userLocationListener = object : UserLocationObjectListener {
        override fun onObjectAdded(userLocationView: UserLocationView) {
            userLocationView.pin.setIcon(
                ImageProvider.fromResource(this@MainActivity, R.drawable.ic_my_location),
                IconStyle().setAnchor(PointF(0.5f, 0.5f))
                    .setRotationType(RotationType.ROTATE)
                    .setZIndex(1f)
                    .setScale(0.5f)
            )
            userLocationView.accuracyCircle.fillColor = Blue.toArgb()
            lifecycleScope.launch {
                delay(500)
                mapView.moveToUser(userLocationLayer)
                userLocationLayer.cameraPosition()?.target?.run {
                    viewModel.accept(
                        MainScreenIntent.MapIntent.AddressChosen(
                            AddressToChoose.START,
                            Location(
                                latitude,
                                longitude
                            )
                        )
                    )
                }
            }
        }

        override fun onObjectRemoved(p0: UserLocationView) {}

        override fun onObjectUpdated(p0: UserLocationView, p1: ObjectEvent) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        MapKitFactory.initialize(this)
        super.onCreate(savedInstanceState)
        requestLocationPermission()
        mapInit()
        setContent {
            val mainScreenState by viewModel.viewState.collectAsState()
            MainScreen(mainScreenState, mapView, userLocationLayer) {
                viewModel.accept(MainScreenIntent.DismissUserModalSheet)
            }
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

    private fun mapInit() {
        mapView = MapView(this)
        val mapKit = MapKitFactory.getInstance()
        mapKit.resetLocationManagerToDefault()
        userLocationLayer = mapKit.createUserLocationLayer(mapView.mapWindow)
        userLocationLayer.isVisible = true
        userLocationLayer.isHeadingEnabled = true
        userLocationLayer.setObjectListener(userLocationListener)
    }

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                "android.permission.ACCESS_FINE_LOCATION"
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf("android.permission.ACCESS_FINE_LOCATION"), 1
            )
        }
    }
}