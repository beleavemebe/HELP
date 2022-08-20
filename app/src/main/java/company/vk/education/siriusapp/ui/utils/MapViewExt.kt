package company.vk.education.siriusapp.ui.utils

import com.yandex.mapkit.mapview.MapView
import company.vk.education.siriusapp.domain.model.Location

val MapView.pickedLocation: Location
    get() = map.cameraPosition.target.run {
        Location(latitude, longitude)
    }