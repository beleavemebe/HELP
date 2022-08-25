package company.vk.education.siriusapp.ui.screens.main.trip

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.yandex.mapkit.geometry.BoundingBoxHelper
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import company.vk.education.siriusapp.R
import company.vk.education.siriusapp.data.VK_USER_URL
import company.vk.education.siriusapp.domain.model.*
import company.vk.education.siriusapp.ui.LocalMappers
import company.vk.education.siriusapp.ui.screens.main.MainViewModel
import company.vk.education.siriusapp.ui.theme.*

@Composable
fun TripScreen(tripState: TripState, vm: MainViewModel = viewModel()) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(horizontal = Spacing16dp)
            .verticalScroll(rememberScrollState())
    ) {
        Column {
            TopAppBar(
                elevation = 0.dp,
                backgroundColor = Color.White,
                title = {
                    Column {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .padding(top = Spacing16dp), contentAlignment = Alignment.Center
                        ) {
                            Divider(
                                Modifier
                                    .fillMaxWidth(0.25f)
                                    .clip(RoundedCornerShape(Spacing4dp)),
                                color = Color.LightGray,
                                thickness = 3.dp
                            )
                        }
                        Spacer(modifier = Modifier.height(Spacing8dp))
                        val titleMapper = LocalMappers.current.tripScreenTitleMapper
                        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            Text(
                                text = stringResource(id = titleMapper.map(tripState.title)),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                },
            )

            Spacer(modifier = Modifier.height(Spacing8dp))

            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(RoundedCornerShape(16.dp)),
                factory = { context ->
                    MapView(context).also { mapView ->
                        tripState.tripRoutePolyline?.let {
                            val mapObj = mapView.map.mapObjects.addCollection()
                            mapObj.addPolyline(it)
                            mapView.map.run {
                                val camera = cameraPosition(
                                    BoundingBoxHelper.getBounds(it)
                                )
                                move(
                                    CameraPosition(camera.target, camera.zoom - 1f, 0f, 0f)
                                )
                            }
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(Spacing8dp))

            Card(stringResource(id = R.string.route)) {
                ShowRoute(tripState.startAddress, tripState.endAddress)
            }

            Card(stringResource(id = R.string.host)) {
                ShowPassenger(tripState.trip.host, showContacts = true, showRating = true, vm)
            }

            Card(stringResource(id = R.string.participants)) {
                ShowParticipants(tripState.trip.passengers, tripState.trip.freePlaces, vm)
            }

            if (tripState.showControls) {
                TripControls(
                    onEditTripClicked = {},
                    onCancelTripClicked = {}
                )
            }
        }
    }
}

@Composable
@Preview
fun TripControlsPreview() = TripControls(
    onEditTripClicked = {},
    onCancelTripClicked = {}
)

@Composable
fun TripControls(
    onEditTripClicked: () -> Unit,
    onCancelTripClicked: () -> Unit,
) {
    return
    Card(title = stringResource(R.string.actions)) {
        Column {
            Spacer(modifier = Modifier.height(Spacing8dp))
            TripAction(stringResource(R.string.edit),  painterResource(id = R.drawable.ic_edit), Blue, onEditTripClicked)
            Spacer(modifier = Modifier.height(Spacing16dp))
            TripAction(stringResource(R.string.cancel_trip), painterResource(id = R.drawable.ic_trashcan), Red, onCancelTripClicked)
            Spacer(modifier = Modifier.height(Spacing16dp))
        }
    }
}

@Composable
fun TripAction(action: String, iconPainter: Painter, color: Color, onClick: () -> Unit) {
    Spacer(modifier = Modifier.width(Spacing8dp))
    Row {
        Image(painter = iconPainter, contentDescription = action, colorFilter = ColorFilter.tint(color))
        Spacer(modifier = Modifier.width(Spacing16dp))
        Text(text = action, style = AppTypography.text, color = color)
    }
}

@Composable
fun Card(title: String, content: @Composable () -> Unit) {
    Surface(
        shape = RoundedCornerShape(Spacing16dp),
        elevation = 0.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(Spacing8dp)) {
            Text(title, style = AppTypography.headline)
            Spacer(Modifier.height(Spacing8dp))
            content()
        }
    }
}

@Composable
fun ShowRoute(
    startAddress: String,
    endAddress: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = R.drawable.ic_circle),
            contentDescription = ""
        )
        Spacer(modifier = Modifier.width(Spacing8dp))
        Text(
            startAddress,
            style = AppTypography.text,
            color = Color.LightGray,
        )
    }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = R.drawable.ic_circle),
            contentDescription = ""
        )
        Spacer(modifier = Modifier.width(Spacing8dp))
        Text(
            endAddress,
            style = AppTypography.text,
            color = Color.LightGray
        )
    }
}

@Composable
fun ShowParticipants(passengers: List<User>, freePlaces: Int, viewModel: MainViewModel) {
    val passengersWithFree = passengers + List(freePlaces) { null }
    passengersWithFree.forEach {
        ShowPassenger(
            it,
        )
    }
}

@Composable
fun ShowPassenger(
    user: User? = null,
    showContacts: Boolean = false,
    showRating: Boolean = false,
    vm: MainViewModel = viewModel(),
) {
    val contentAlpha = if (user != null) 1.0F else 0.4F
    Row(
        Modifier
            .padding(vertical = Spacing8dp)
            .clickable { vm.showUser(user ?: return@clickable) },
        verticalAlignment = Alignment.CenterVertically,

    ) {
        AsyncImage(
            model = user?.imageUrl ?: "",
            contentDescription = stringResource(id = R.string.profile_pic),
            placeholder = painterResource(id = R.drawable.profile_avatar_placeholder),
            error = painterResource(id = R.drawable.profile_avatar_placeholder),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(Spacing32dp)
                .clip(CircleShape)
                .alpha(contentAlpha)
        )
        Spacer(Modifier.width(Spacing12dp))
        Column(horizontalAlignment = Alignment.Start) {
            Text(
                text = user?.name ?: stringResource(R.string.free_place),
                style = AppTypography.text,
                modifier = Modifier.alpha(contentAlpha)
            )
            if (showRating) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_rating),
                        contentDescription = stringResource(R.string.rating)
                    )
                    Spacer(modifier = Modifier.width(Spacing4dp))
                    Text(
                        text = user?.rating?.toInt().toString() + "%",
                        style = AppTypography.caption2.copy(color = TextHint)
                    )
                }
            }
        }
    }
}
