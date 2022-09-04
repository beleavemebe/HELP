package company.vk.education.siriusapp.ui.library.yandexmaps

import com.yandex.mapkit.Animation
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Polyline

sealed class MapViewEffect {
    companion object {
        const val ZOOM_DEFAULT = 18.0f
        const val AZIMUTH_DEFAULT = 0f
        const val TILT_DEFAULT = 0f
        const val ANIMATION_DURATION_DEFAULT = 0.5f
    }

    data class MoveToPoint(
        val point: Point,
        val zoom: Float = ZOOM_DEFAULT,
        val azimuth: Float = AZIMUTH_DEFAULT,
        val tilt: Float = TILT_DEFAULT,
        val animationType: Animation.Type = Animation.Type.SMOOTH,
        val animationDuration: Float = ANIMATION_DURATION_DEFAULT
    ) : MapViewEffect()

    data class RenderRoute(
        val polyline: Polyline,
        val moveToRoute: Boolean = false
    ) : MapViewEffect()
}
