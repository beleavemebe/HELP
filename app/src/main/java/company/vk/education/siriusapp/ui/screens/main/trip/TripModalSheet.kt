package company.vk.education.siriusapp.ui.screens.main.trip

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

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