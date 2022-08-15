package company.vk.education.siriusapp.domain.model

data class Trip(
    val id: String,
    val route: TripRoute,
    val totalPrice: Int,
    val freePlaces: Int,
    val host: User,
    val participants: List<User>,
    val taxiServiceInfo: TaxiService
)
