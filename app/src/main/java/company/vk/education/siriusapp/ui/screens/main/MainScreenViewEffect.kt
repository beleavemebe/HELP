package company.vk.education.siriusapp.ui.screens.main

import company.vk.education.siriusapp.domain.model.Location
import company.vk.education.siriusapp.ui.base.BaseViewEffect

open class MainScreenViewEffect private constructor() : BaseViewEffect {
    data class MoveMapToLocation(val location: Location) : MainScreenViewEffect()
}
