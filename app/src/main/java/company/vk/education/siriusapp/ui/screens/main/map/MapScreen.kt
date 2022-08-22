package company.vk.education.siriusapp.ui.screens.main.map

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import company.vk.education.siriusapp.R
import company.vk.education.siriusapp.ui.screens.main.AddressToChoose
import company.vk.education.siriusapp.ui.screens.main.MainScreenIntent
import company.vk.education.siriusapp.ui.screens.main.MainViewModel
import company.vk.education.siriusapp.ui.theme.*
import company.vk.education.siriusapp.ui.utils.log
import company.vk.education.siriusapp.ui.utils.moveToUser
import company.vk.education.siriusapp.ui.utils.pickedLocation
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


val MainViewModel.mapState: StateFlow<MapViewState>
    get() = viewState
        .map { it.mapState }
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            initialState.mapState
        )

@Composable
fun MapScreen(
    mapView: MapView,
    userLocationLayer: UserLocationLayer,
    viewModel: MainViewModel = viewModel()
) {
    val state by viewModel.mapState.collectAsState()

    Map(
        mapView = mapView,
        userLocationLayer = userLocationLayer,
        state = state,
        onProfileClicked = { viewModel.accept(MainScreenIntent.MapIntent.ShowProfile) },
        onLocationChosen = { addressToChoose ->
            val location = mapView.pickedLocation
            viewModel.accept(
                MainScreenIntent.MapIntent.AddressChosen(addressToChoose, location)
            )
        }
    )

    LaunchedEffect(key1 = null, block = {
        log("launched effect")
        mapView.map.addCameraListener(viewModel.cameraListener)
    })
}


@Composable
fun Map(
    mapView: MapView,
    userLocationLayer: UserLocationLayer,
    state: MapViewState,
    onLocationChosen: (AddressToChoose) -> Unit,
    onProfileClicked: () -> Unit
) {
    Box(contentAlignment = Alignment.TopEnd) {
        AndroidView(factory = { mapView })
        ChooseLocation(state = state, map = mapView, onClick = onLocationChosen)
        ProfileView(state = state, onProfileClicked = onProfileClicked)
        ToUserLocationFAB(mapView, userLocationLayer)
    }
}

@Composable
private fun ToUserLocationFAB(
    mapView: MapView,
    userLocationLayer: UserLocationLayer
) {
    Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier.padding(Spacing16dp).fillMaxSize()) {
        Column {
            Button(
                onClick = { mapView.moveToUser(userLocationLayer) },
                modifier = Modifier
                    .clip(CircleShape)
                    .size(FabSize)
                    .border(2.dp, Blue, CircleShape),
                colors = ButtonDefaults.buttonColors(backgroundColor = OnBlue),
                contentPadding = PaddingValues(Spacing8dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_location),
                    contentDescription = stringResource(id = R.string.my_location),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillHeight
                )
            }
            Spacer(Modifier.height(175.dp + Spacing32dp + Spacing8dp))
        }
    }
}

@Composable
fun ChooseLocation(state: MapViewState, map: MapView, onClick: (AddressToChoose) -> Unit) {
    if (state.isChoosingAddress) {
        require(state.addressToChoose != null) {
            "State does not specify which address is being chosen. $state"
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.pin),
                contentDescription = "pin",
                modifier = Modifier
                    .size(64.dp)
                    .offset(0.dp, (-24).dp)
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            val pickAddress = when (state.addressToChoose) {
                AddressToChoose.START -> stringResource(id = R.string.pick_start_address)
                AddressToChoose.END -> stringResource(id = R.string.pick_end_address)
            }

            Text(pickAddress, style = AppTypography.subhead)
            Text(
                state.currentlyChosenAddress ?: stringResource(R.string.calculating),
                style = AppTypography.headlineMedium
            )
        }
        Box(modifier = Modifier.fillMaxSize().padding(Spacing16dp, 0.dp), contentAlignment = Alignment.BottomCenter) {
            Column {
                Button(
                    onClick = { onClick(state.addressToChoose) },
                    colors = ButtonDefaults.buttonColors(Blue),
                    shape = Shapes.medium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(FabSize)
                ) {
                    Text(
                        stringResource(R.string.pick),
                        style = AppTypography.subheadMedium,
                        color = OnBlue
                    )
                }
                Spacer(modifier = Modifier.height(Spacing16dp))
            }
        }
    }
}

@Composable
fun ProfileView(state: MapViewState, onProfileClicked: () -> Unit) {
    if (state.isChoosingAddress) return
    AsyncImage(
        model = state.profilePicUrl,
        contentDescription = stringResource(id = R.string.profile_pic),
        placeholder = painterResource(id = R.drawable.profile_avatar_placeholder),
        error = painterResource(id = R.drawable.profile_avatar_placeholder),
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .padding(top = Spacing16dp, end = Spacing16dp)
            .size(FabSize)
            .clip(CircleShape)
            .border(2.dp, Blue, CircleShape)
            .clickable { onProfileClicked() }
    )
}

@Composable
fun ProfileView(url: String? = null, onClick: () -> Unit) {
    AsyncImage(
        model = url,
        contentDescription = stringResource(id = R.string.profile_pic),
        placeholder = painterResource(
            id = R.drawable.profile_avatar_placeholder
        ),
        error = painterResource(
            id = R.drawable.profile_avatar_placeholder
        ),
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .padding(top = Spacing16dp, end = Spacing16dp)
            .size(64.dp)
            .clip(CircleShape)
            .clickable { onClick() }
    )
}
