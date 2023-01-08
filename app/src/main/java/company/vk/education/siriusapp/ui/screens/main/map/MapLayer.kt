package company.vk.education.siriusapp.ui.screens.main.map

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.yandex.mapkit.MapKitFactory
import company.vk.education.siriusapp.R
import company.vk.education.siriusapp.ui.activity.LocationPermissionStatus.*
import company.vk.education.siriusapp.ui.library.yandexmaps.YandexMap
import company.vk.education.siriusapp.ui.screens.main.*
import company.vk.education.siriusapp.ui.theme.*
import company.vk.education.siriusapp.ui.utils.log

@Composable
fun MapLayer(
    state: MapViewState,
) {
    Box(contentAlignment = Alignment.TopEnd) {
        Map()
        ChooseLocation(state = state)
        ProfileView(state = state)
        ToUserLocationFAB()
    }
}

@Composable
private fun ToUserLocationFAB() {
    val intentConsumer = LocalMainScreenIntentConsumer.current
    val locationPermissionHandler = LocalMainScreenDeps.current.locationPermissionHandler
    val locationPermissionStatus by locationPermissionHandler.status.collectAsState()
    LaunchedEffect(key1 = locationPermissionStatus) {
        if (locationPermissionStatus == GRANTED) {
            intentConsumer.consume(MainScreenIntent.MapIntent.LocationPermissionGranted)
        }
    }

    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier
            .padding(Spacing16dp)
            .fillMaxSize()
    ) {
        Column {
            Button(
                onClick = {
                    when (locationPermissionStatus) {
                        GRANTED -> {
                            intentConsumer.consume(MainScreenIntent.MapIntent.MoveToUserLocation)
                        }
                        NEEDS_EXPLANATION -> {
                            log("explain") // todo
                            locationPermissionHandler.requestLocationPermissions()
                        }
                        PERMANENTLY_DENIED -> {
                            log("explain & lead to settings") // todo
                        }
                        else -> {
                            locationPermissionHandler.requestLocationPermissions()
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = OnBlue),
                contentPadding = PaddingValues(Spacing8dp),
                modifier = Modifier
                    .clip(CircleShape)
                    .size(FabSize)
            ) {
                val iconTint = if (locationPermissionStatus == GRANTED) Blue else Grey
                Image(
                    painter = painterResource(id = R.drawable.ic_place),
                    contentDescription = stringResource(id = R.string.my_location),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillHeight,
                    colorFilter = ColorFilter.tint(iconTint)
                )
            }
            Spacer(Modifier.height(175.dp + Spacing32dp + Spacing8dp))
        }
    }
}

@Composable
fun ChooseLocation(
    state: MapViewState
) {
    if (state.isChoosingAddress) {
        val intentConsumer = LocalMainScreenIntentConsumer.current
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
                .padding(Spacing16dp)
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
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing16dp, 0.dp)
        ) {
            Column {
                Button(
                    onClick = {
                        state.currentlyChosenLocation?.let {
                            intentConsumer.consume(
                                MainScreenIntent.MapIntent.AddressChosen(state.addressToChoose, it)
                            )
                        }
                    },
                    colors = ButtonDefaults.buttonColors(Blue),
                    shape = Shapes.medium,
                    elevation = null,
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
fun ProfileView(state: MapViewState) {
    if (state.isChoosingAddress) return
    val intentConsumer = LocalMainScreenIntentConsumer.current
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
            .border(2.dp, Grey, CircleShape)
            .clickable {
                intentConsumer.consume(MainScreenIntent.MapIntent.ShowMyProfile)
            }
    )
}

@Composable
fun Map() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val intentConsumer = LocalMainScreenIntentConsumer.current
    val effectSource = LocalMainScreenViewEffectSource.current

    val cameraListener by remember {
        lazy {
            CurrentlyPickedLocationCameraListener(intentConsumer)
        }
    }

    val userLocationListener by remember {
        lazy {
            UserLocationListener(context, scope) {
                intentConsumer.consume(MainScreenIntent.MapIntent.UserLocationAcquired(it))
            }
        }
    }

    YandexMap(effectSource.viewEffects, ::toMapViewEffect) {
        map.isRotateGesturesEnabled = false
        map.addCameraListener(cameraListener)
        MapKitFactory.getInstance()
            .createUserLocationLayer(mapWindow).apply {
                isVisible = true
                isHeadingEnabled = true
                setObjectListener(userLocationListener)
            }
    }
}

