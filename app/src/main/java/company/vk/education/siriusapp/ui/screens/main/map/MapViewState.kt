package company.vk.education.siriusapp.ui.screens.main.map

import company.vk.education.siriusapp.domain.model.Location
import company.vk.education.siriusapp.ui.screens.main.AddressToChoose

data class MapViewState (
    val profilePicUrl: String? = null,
    val isChoosingAddress: Boolean = false,
    val addressToChoose: AddressToChoose? = null,
    val currentlyChosenAddress: String? = null,
    val currentlyChosenLocation: Location? = null,
)