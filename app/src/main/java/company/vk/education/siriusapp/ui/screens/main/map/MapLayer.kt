package company.vk.education.siriusapp.ui.screens.main.map

import android.Manifest
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.google.accompanist.permissions.*
import com.yandex.mapkit.MapKitFactory
import company.vk.education.siriusapp.R
import company.vk.education.siriusapp.ui.activity.MainActivity
import company.vk.education.siriusapp.ui.fuckpermissiosn.LocationPermissionStatus
import company.vk.education.siriusapp.ui.fuckpermissiosn.LocationPermissionTracker
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

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun ToUserLocationFAB() {
    Sample()
    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )
    val intentConsumer = LocalMainScreenIntentConsumer.current
    Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier
        .padding(Spacing16dp)
        .fillMaxSize()) {
        Column {
            Button(
                onClick = {
                    if (locationPermissionsState.allPermissionsGranted) {
                        intentConsumer.consume(MainScreenIntent.MapIntent.MoveToUserLocation)
                    } else {
                        if (locationPermissionsState.shouldShowRationale) {
                            log("rationale")
                        } else {
                            log("no rationale")
                        }

                        if (locationPermissionsState.isPermanentlyDenied()) {
                            log("permanently denied")
                        }

                        locationPermissionsState.launchMultiplePermissionRequest()
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = OnBlue),
                contentPadding = PaddingValues(Spacing8dp),
                modifier = Modifier
                    .clip(CircleShape)
                    .size(FabSize)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_place),
                    contentDescription = stringResource(id = R.string.my_location),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillHeight,
                    colorFilter = ColorFilter.tint(Blue)
                )
            }
            Spacer(Modifier.height(175.dp + Spacing32dp + Spacing8dp))
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
private fun MultiplePermissionsState.isPermanentlyDenied(): Boolean {
    return permissions.all {
        it.isPermanentlyDenied()
    }
}

@OptIn(ExperimentalPermissionsApi::class)
private fun PermissionState.isPermanentlyDenied(): Boolean {
    return status.isGranted.not() && status.shouldShowRationale.not()
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun Sample(

) {
    val status: LocationPermissionStatus? by MainActivity.fuckYou!!.get().status.collectAsState()
    Text(text = status.toString())

    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    if (locationPermissionsState.allPermissionsGranted) {
//        Text("Thanks! I can access your exact location :D")
    } else {
        Column {
            val allPermissionsRevoked =
                locationPermissionsState.permissions.size ==
                        locationPermissionsState.revokedPermissions.size

            val textToShow = if (!allPermissionsRevoked) {
                // If not all the permissions are revoked, it's because the user accepted the COARSE
                // location permission, but not the FINE one.
                "Yay! Thanks for letting me access your approximate location. " +
                        "But you know what would be great? If you allow me to know where you " +
                        "exactly are. Thank you!"
            } else if (locationPermissionsState.shouldShowRationale) {
                // Both location permissions have been denied
                "Getting your exact location is important for this app. " +
                        "Please grant us fine location. Thank you :D"
            } else {
                // First time the user sees this feature or the user doesn't want to be asked again
                "This feature requires location permission"
            }

            val buttonText = if (!allPermissionsRevoked) {
                "Allow precise location"
            } else {
                "Request permissions"
            }

//            Text(text = textToShow)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
//                locationPermissionsState.launchMultiplePermissionRequest()
                MainActivity.fuckYou!!.get().requestLocationPermissions()
            }) {
                Text(buttonText)
            }
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

