package company.vk.education.siriusapp.ui.activity

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.image.ImageProvider
import company.vk.education.siriusapp.R
import company.vk.education.siriusapp.ui.screens.main.MainScreen
import company.vk.education.siriusapp.ui.screens.main.MainScreenIntent
import company.vk.education.siriusapp.ui.screens.main.MainScreenViewEffect
import company.vk.education.siriusapp.ui.screens.main.MainViewModel
import company.vk.education.siriusapp.ui.theme.Blue
import company.vk.education.siriusapp.ui.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()

    private lateinit var mapView: MapView
    private lateinit var userLocationLayer: UserLocationLayer

    private val userLocationListener = object : UserLocationObjectListener {
        override fun onObjectAdded(userLocationView: UserLocationView) {
            userLocationView.pin.setIcon(
                ImageProvider.fromResource(
                    this@MainActivity,
                    R.drawable.ic_my_location
                ),
            )
            userLocationView.accuracyCircle.fillColor = Blue.copy(alpha = 0.1F).toArgb()
            lifecycleScope.launch {
                delay(250)
                val userLocation = userLocationLayer.cameraPosition()
                    ?.pickedLocation ?: return@launch
                viewModel.accept(MainScreenIntent.MapIntent.UserLocationAcquired(userLocation))
            }
        }

        override fun onObjectRemoved(p0: UserLocationView) {}

        override fun onObjectUpdated(p0: UserLocationView, p1: ObjectEvent) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        MapKitFactory.initialize(this)
        DirectionsFactory.initialize(this)
        super.onCreate(savedInstanceState)
        requestLocationPermission()
        mapInit()
        setContent {
            val mainScreenState by viewModel.viewState.collectAsState()
            viewModel.collectViewEffects(::handleViewEffect)
            CompositionLocalProvider(
                LocalTaxiServiceToStringResMapper.let { it provides it.current },
                LocalTaxiVehicleClassToStringResMapper.let { it provides it.current },
            ) {
                MainScreen(
                    state = mainScreenState,
                    mapView = mapView,
                    userLocationLayer = userLocationLayer,
                    onDismissUserSheet = {
                        viewModel.accept(MainScreenIntent.DismissUserModalSheet)
                    },
                    onDismissTripSheet = {
                        viewModel.accept(MainScreenIntent.DismissTripModalSheet)
                    }
                )
            }
        }
    }

    private fun handleViewEffect(viewEffect: MainScreenViewEffect) {
        when (viewEffect) {
            is MainScreenViewEffect.MoveMapToLocation -> {
                mapView.moveToLocation(viewEffect.location)
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
        mapView.map.isRotateGesturesEnabled = false
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