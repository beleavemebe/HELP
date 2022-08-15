package company.vk.education.siriusapp.domain.model

data class UserContacts(
    val phoneNumber: String,
    val vkLink: String? = null,
    val tgLink: String? = null,
)
