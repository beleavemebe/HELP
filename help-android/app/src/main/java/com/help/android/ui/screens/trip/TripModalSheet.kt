package com.help.android.ui.screens.trip

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.help.android.ui.screens.trip.model.TripState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TripModalSheet(tripState: TripState, sheetState: ModalBottomSheetState) {
    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(32.dp, 32.dp, 0.dp, 0.dp),
        sheetContent = {
            TripScreenContent(tripState)
        }
    ) {}
}