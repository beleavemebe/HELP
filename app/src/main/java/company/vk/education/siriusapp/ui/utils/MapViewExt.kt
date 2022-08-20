package company.vk.education.siriusapp.ui.utils

import com.yandex.mapkit.Animation
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import company.vk.education.siriusapp.domain.model.Location

val MapView.pickedLocation: Location
    get() = map.cameraPosition.target.run {
        Location(latitude, longitude)
    }

fun MapView.moveToUser(layer: UserLocationLayer, zoom: Float = 18.0F) {
    map.move(
        CameraPosition(
            layer.cameraPosition()?.target ?: Point(0.0, 0.0),
            zoom,
            0f,
            0f
        ),
        Animation(Animation.Type.SMOOTH, 0.5f),
        null
    )
}