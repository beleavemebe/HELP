package company.vk.education.siriusapp.ui.screens.main

import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable


@Composable
@OptIn(ExperimentalMaterialApi::class)
fun MainScreen(
    Content: @Composable () -> Unit,
    SheetContent: @Composable () -> Unit,
) {
    BottomSheetScaffold(
        sheetContent = { SheetContent() }
    ) {
        Content()
    }
}