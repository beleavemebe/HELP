package company.vk.education.siriusapp.ui.library.yandexmaps

import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.BoundingBoxHelper
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Polyline
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import company.vk.education.siriusapp.ui.base.BaseViewEffect
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

@Composable
fun <VE : BaseViewEffect> YandexMap(
    effects: Flow<VE>,
    toMapViewEffect: (VE) -> MapViewEffect?, // FIXME: use Mapper<VE, MapViewEffect?> instance perhaps?
    modifier: Modifier = Modifier,
    initialize: MapView.() -> Unit
) {
    var lifecycleOwner = LocalLifecycleOwner.current
    AndroidView(
        modifier = modifier,
        factory = { context ->
            val mapView = MapView(context).also {
                lifecycleOwner = (context as? LifecycleOwner) ?: lifecycleOwner
                lifecycleOwner.lifecycle.addObserver(
                    MapKitInitializer(initialize, it, context)
                )
            }

            fun renderViewEffect(viewEffect: MapViewEffect) {
                when (viewEffect) {
                    is MapViewEffect.MoveToPoint -> {
                        mapView.moveToPoint(
                            viewEffect.point,
                            viewEffect.zoom,
                            viewEffect.azimuth,
                            viewEffect.tilt,
                            viewEffect.animationType,
                            viewEffect.animationDuration,
                        )
                    }
                    is MapViewEffect.RenderRoute -> {
                        mapView.renderRoute(viewEffect.polyline, viewEffect.moveToRoute)
                    }
                    is MapViewEffect.LocationPermissionGranted -> {
                        MapKitFactory.getInstance().resetLocationManagerToDefault()
                    }
                }
            }

            val effectReceiver = ComposeView(context).apply {
                setContent {
                    LaunchedEffect(key1 = true) {
                        effects.map(toMapViewEffect).filterNotNull()
                            .collect(::renderViewEffect)
                    }
                }
            }

            FrameLayout(context).apply {
                addView(mapView)
                addView(effectReceiver)
            }
        }
    )
}

fun MapView.renderRoute(
    polyline: Polyline,
    zoomTheRoute: Boolean
) {
    val mapObj = map.mapObjects.addCollection()
    mapObj.addPolyline(polyline)
    if (zoomTheRoute) {
        val camera = map.cameraPosition(BoundingBoxHelper.getBounds(polyline))
        map.move(
            CameraPosition(
                camera.target,
                camera.zoom - 0.25f,
                MapViewEffect.AZIMUTH_DEFAULT,
                MapViewEffect.TILT_DEFAULT
            )
        )
    }
}

fun MapView.moveToPoint(
    point: Point,
    zoom: Float,
    azimuth: Float,
    tilt: Float,
    animationType: Animation.Type,
    animationDuration: Float,
) {
    map.move(
        CameraPosition(
            point,
            zoom,
            azimuth,
            tilt
        ),
        Animation(
            animationType,
            animationDuration,
        ),
        null
    )
}