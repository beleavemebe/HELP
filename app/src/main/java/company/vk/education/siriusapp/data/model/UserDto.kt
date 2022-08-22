package company.vk.education.siriusapp.data.model

data class UserDto(
    val id: String? = null,
    val name: String? = null,
    val imageUrl: String? = null,
    val phone: String? = null,
    val vkLink: String? = null,
    val tgLink: String? = null,
    val rating: Double? = null,
)