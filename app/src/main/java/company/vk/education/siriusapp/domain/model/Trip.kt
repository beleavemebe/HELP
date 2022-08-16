package company.vk.education.siriusapp.domain.model

import java.util.*

data class Trip(
    val id: String = UUID.randomUUID().toString(),
    val route: TripRoute,
    val totalPrice: Int,
    val freePlaces: Int,
    val host: User,
    val passengers: List<User>,
    val taxiServiceInfo: TaxiService
)
