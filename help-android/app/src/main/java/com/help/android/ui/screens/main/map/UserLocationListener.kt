package com.help.android.ui.screens.main.map

import android.content.Context
import androidx.compose.ui.graphics.toArgb
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.image.ImageProvider
import com.help.android.R
import com.help.android.domain.model.Location
import com.help.android.ui.theme.Blue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class UserLocationListener(
    private val context: Context,
    private val scope: CoroutineScope,
    private val onUserLocationChanged: (Location) -> Unit
) : UserLocationObjectListener {
    override fun onObjectAdded(userLocationView: UserLocationView) {
        drawUserLocation(userLocationView)
        scope.launch {
            delay(250)
            val userLocation = userLocationView.pin.geometry.run {
                Location(latitude, longitude)
            }
            onUserLocationChanged(userLocation)
        }
    }

    private fun drawUserLocation(userLocationView: UserLocationView) {
        userLocationView.pin.setIcon(ImageProvider.fromResource(context, R.drawable.ic_my_location))
        userLocationView.accuracyCircle.fillColor = Blue.copy(alpha = 0.1F).toArgb()
    }

    override fun onObjectUpdated(p0: UserLocationView, p1: ObjectEvent) {}
    override fun onObjectRemoved(p0: UserLocationView) {}
}