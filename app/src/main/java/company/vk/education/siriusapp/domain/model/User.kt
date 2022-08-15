package company.vk.education.siriusapp.domain.model

data class User(
    val id: String,
    val name: String,
    val imageUrl: String,
    val contact: UserContacts,
    val rating: Double,
)