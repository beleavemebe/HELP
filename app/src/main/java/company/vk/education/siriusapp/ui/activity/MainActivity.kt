package company.vk.education.siriusapp.ui.activity

import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PointF
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.graphics.colorspace.ColorSpaces
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
import company.vk.education.siriusapp.domain.service.AuthService
import company.vk.education.siriusapp.ui.screens.main.bottomsheet.BottomSheetScreen
import company.vk.education.siriusapp.ui.screens.main.map.MapScreen
import company.vk.education.siriusapp.ui.theme.Blue
import company.vk.education.siriusapp.ui.theme.Blue900
import company.vk.education.siriusapp.ui.utils.log
import company.vk.education.siriusapp.ui.utils.moveToUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
@OptIn(ExperimentalMaterialApi::class)
class MainActivity : AppCompatActivity() {

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
            }
        }

        override fun onObjectRemoved(p0: UserLocationView) {}

        override fun onObjectUpdated(p0: UserLocationView, p1: ObjectEvent) {}
    }

    @Inject
    lateinit var authService: AuthService

    override fun onCreate(savedInstanceState: Bundle?) {
        MapKitFactory.initialize(this)
        super.onCreate(savedInstanceState)
        requestLocationPermission()
        mapInit()
        setContent {
            BottomSheetScaffold(
                sheetContent = {
                    BottomSheetScreen()
                }
            ) {
                MapScreen(mapView, userLocationLayer)
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