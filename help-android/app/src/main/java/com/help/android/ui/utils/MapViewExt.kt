package com.help.android.ui.utils

import com.yandex.mapkit.map.CameraPosition
import com.help.android.domain.model.Location

val CameraPosition.pickedLocation: Location
    get() = target.run {
        Location(latitude, longitude)
    }