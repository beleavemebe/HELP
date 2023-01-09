package com.help.android.ui.screens.main.map

import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.CameraUpdateReason
import com.yandex.mapkit.map.Map
import com.help.android.ui.base.IntentConsumer
import com.help.android.ui.screens.main.MainScreenIntent
import com.help.android.ui.utils.pickedLocation

class CurrentlyPickedLocationCameraListener(
    private val intentConsumer: IntentConsumer<MainScreenIntent>
) : CameraListener {
    private var isMoving = false

    override fun onCameraPositionChanged(
        p0: Map,
        cameraPosition: CameraPosition,
        p2: CameraUpdateReason,
        finished: Boolean
    ) {
        if (finished) {
            isMoving = false
            intentConsumer.consume(
                MainScreenIntent.MapIntent.UpdatePickedLocation(cameraPosition.pickedLocation)
            )
        } else {
            if (isMoving) return
            isMoving = true
            intentConsumer.consume(
                MainScreenIntent.MapIntent.InvalidateChosenAddress
            )
        }
    }
}