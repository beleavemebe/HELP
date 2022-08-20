package company.vk.education.siriusapp.ui.screens.main

import company.vk.education.siriusapp.ui.base.BaseViewState
import company.vk.education.siriusapp.ui.screens.main.bottomsheet.BottomSheetState
import company.vk.education.siriusapp.ui.screens.main.map.MapViewState

data class MainScreenState(
    val mapState: MapViewState,
    val bottomSheetState: BottomSheetState,
) : BaseViewState