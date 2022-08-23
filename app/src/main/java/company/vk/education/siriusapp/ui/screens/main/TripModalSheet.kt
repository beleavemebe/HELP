package company.vk.education.siriusapp.ui.screens.main

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import company.vk.education.siriusapp.domain.model.Trip

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TripModalSheet(trip: Trip, sheetState: ModalBottomSheetState) {
    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(32.dp, 32.dp, 0.dp, 0.dp),
        sheetContent = {
            Text(text = trip.toString())
        }
    ) {}
}