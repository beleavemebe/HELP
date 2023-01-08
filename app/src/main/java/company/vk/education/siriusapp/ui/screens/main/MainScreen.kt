package company.vk.education.siriusapp.ui.screens.main

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import company.vk.education.siriusapp.ui.screens.Screens
import company.vk.education.siriusapp.ui.screens.main.bottomsheet.BottomSheet
import company.vk.education.siriusapp.ui.screens.main.bottomsheet.LocalTaxiInfoMappers
import company.vk.education.siriusapp.ui.screens.main.map.MapLayer
import company.vk.education.siriusapp.ui.theme.Spacing32dp
import company.vk.education.siriusapp.ui.utils.log
import company.vk.education.siriusapp.ui.utils.collectViewEffects

fun NavGraphBuilder.mainScreen(
    navController: NavHostController,
    mainScreenDeps: MainScreenDeps
) {
    composable(route = Screens.Main.route) {
        val viewModel = hiltViewModel<MainViewModel>()
        viewModel.collectViewEffects {
            log("render view effect")
            when (it) {
                is MainScreenViewEffect.Navigate -> navController.navigate(it.route)
            }
        }

        val mainScreenState by viewModel.viewState.collectAsState()

        CompositionLocalProvider(
            LocalMainScreenIntentConsumer provides viewModel,
            LocalMainScreenViewEffectSource provides viewModel,
            LocalMainScreenDeps provides mainScreenDeps,
            LocalTaxiInfoMappers provides mainScreenDeps.bottomSheetMappers,
        ) {
            MainScreen(mainScreenState)
        }
    }
}

@Composable
@OptIn(ExperimentalMaterialApi::class)
fun MainScreen(
    state: MainScreenState,
) {
    val intentConsumer = LocalMainScreenIntentConsumer.current

    val bottomSheetState = rememberBottomSheetState(
        initialValue = BottomSheetValue.Collapsed,
        confirmStateChange = { pendingState ->
            when (pendingState) {
                BottomSheetValue.Collapsed -> {
                    intentConsumer.consume(MainScreenIntent.CollapseBottomSheet)
                }
                BottomSheetValue.Expanded -> {
                    intentConsumer.consume(MainScreenIntent.ExpandBottomSheet)
                }
            }
            false
        }
    )
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetState)
    val sheetPeekHeight = if (state.mapState.isChoosingAddress) 0.dp else 186.dp

    LaunchedEffect(key1 = state.isBottomSheetExpanded) {
        if (state.isBottomSheetExpanded) {
            bottomSheetState.expand()
        } else {
            bottomSheetState.collapse()
        }
    }

    BottomSheetScaffold(
        sheetContent = {
            BottomSheet(state.bottomSheetScreenState)
        },
        scaffoldState = scaffoldState,
        sheetPeekHeight = sheetPeekHeight,
        sheetShape = RoundedCornerShape(Spacing32dp),
    ) {
        MapLayer(state.mapState)
    }
}
