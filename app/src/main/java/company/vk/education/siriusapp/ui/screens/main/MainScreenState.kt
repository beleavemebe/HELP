package company.vk.education.siriusapp.ui.screens.main

import company.vk.education.siriusapp.ui.base.BaseViewState
import company.vk.education.siriusapp.ui.screens.main.bottomsheet.BottomSheetScreenState
import company.vk.education.siriusapp.ui.screens.main.map.MapViewState

data class MainScreenState(
    val mapState: MapViewState,
    val isBottomSheetExpanded: Boolean = false,
    val bottomSheetScreenState: BottomSheetScreenState,
//    val isShowingUser: Boolean = false,
//    val userState: UserState? = null,
//    val tripState: TripState? = null
) : BaseViewState