package company.vk.education.siriusapp.ui.screens.main

import company.vk.education.siriusapp.domain.model.User
import company.vk.education.siriusapp.ui.base.BaseViewState
import company.vk.education.siriusapp.ui.screens.main.bottomsheet.BottomSheetScreenState
import company.vk.education.siriusapp.ui.screens.main.map.MapViewState

data class MainScreenState(
    val mapState: MapViewState,
    val isBottomSheetExpanded: Boolean = false,
    val bottomSheetScreenState: BottomSheetScreenState,
    val isShowingProfile: Boolean = false,
    val profileToShow: User? = null
) : BaseViewState