package company.vk.education.siriusapp.domain.model

import java.util.*

data class Trip(
    val id: String = UUID.randomUUID().toString(),
    val route: TripRoute,
    val freePlaces: Int,
    val host: User,
    val passengers: List<User>,
    val taxiService: TaxiService,
    val taxiVehicleClass: TaxiVehicleClass,
)
