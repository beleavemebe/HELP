package company.vk.education.siriusapp.ui.screens.main

import company.vk.education.siriusapp.ui.base.BaseViewState

data class MainScreenState(
    val mapState: MainViewState.MapViewState,
    val bottomSheetState: MainViewState.BottomSheetState,
) : BaseViewState