package company.vk.education.siriusapp.ui.utils

import com.yandex.mapkit.geometry.Point
import company.vk.education.siriusapp.domain.model.Location

fun Location.toPoint() = Point(latitude, longitude)