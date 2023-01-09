package com.help.android.ui.screens.main.map

import com.help.android.domain.model.Location
import com.help.android.ui.screens.main.AddressToChoose

data class MapViewState (
    val profilePicUrl: String? = null,
    val isChoosingAddress: Boolean = false,
    val addressToChoose: AddressToChoose? = null,
    val currentlyChosenAddress: String? = null,
    val currentlyChosenLocation: Location? = null,
)