package company.vk.education.siriusapp.data.appdata

import kotlinx.serialization.Serializable

@Serializable
data class AppData(
    val wasLocationPermissionRequested: Boolean = false
)
