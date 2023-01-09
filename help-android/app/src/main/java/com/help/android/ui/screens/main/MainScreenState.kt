package com.help.android.ui.screens.main

import com.help.android.ui.base.BaseViewState
import com.help.android.ui.screens.main.bottomsheet.BottomSheetScreenState
import com.help.android.ui.screens.main.map.MapViewState

data class MainScreenState(
    val mapState: MapViewState,
    val isBottomSheetExpanded: Boolean = false,
    val bottomSheetScreenState: BottomSheetScreenState,
) : BaseViewState