package company.vk.education.siriusapp.core

import company.vk.education.siriusapp.domain.model.Location
import company.vk.education.siriusapp.domain.model.TripRoute
import kotlin.math.abs
import kotlin.math.sqrt

infix fun Location.dist(location: Location): Double {
    val x = abs(location.latitude - latitude)
    val y = abs(location.longitude - longitude)
    return sqrt(x * x + y * y)
}

infix fun TripRoute.dist(route: TripRoute) =
    (startLocation dist route.startLocation) + (endLocation dist route.endLocation)

// we don't live in flat world and can't go through by straight line =)
// :todo: request yandex to get distance
const val SHIFT = 20
const val KM_IN_ONE = 111

fun Double.meters(exact: Boolean = false) = times(KM_IN_ONE + if (exact) 0 else SHIFT) * 1000
