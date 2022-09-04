package company.vk.education.siriusapp.ui.utils

import com.yandex.mapkit.map.CameraPosition
import company.vk.education.siriusapp.domain.model.Location

val CameraPosition.pickedLocation: Location
    get() = target.run {
        Location(latitude, longitude)
    }