package company.vk.education.siriusapp.ui.screens.main.trip

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.yandex.mapkit.geometry.BoundingBoxHelper
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import company.vk.education.siriusapp.R
import company.vk.education.siriusapp.data.VK_USER_URL
import company.vk.education.siriusapp.domain.model.*
import company.vk.education.siriusapp.ui.theme.*

@Composable
fun TripScreen(tripState: TripState) {
    Scaffold(
        topBar = {
            TopAppBar(
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
                        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            Text(
                                text = stringResource(id = R.string.current_trip),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                },
                backgroundColor = Color.White,
                elevation = 0.dp
            )
        }, content = {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(Spacing16dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {
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
                    Card(stringResource(id = R.string.trip)) {
                        ShowRoute(tripState.startAddress, tripState.endAddress)
                    }
                    Card(stringResource(id = R.string.host)) {
                        ShowPassenger(tripState.trip.host, true)
                    }
                    Card(stringResource(id = R.string.participants)) {
                        ShowParticipants(tripState.trip.passengers, tripState.trip.freePlaces)
                    }
                }
            }
        })
}

@Composable
fun Card(title: String, content: @Composable () -> Unit) {
    Surface(
        shape = RoundedCornerShape(Spacing16dp),
        elevation = 0.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(Spacing16dp)) {
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
    Text(
        startAddress,
        style = AppTypography.text,
        color = Color.LightGray
    )
    Text(
        endAddress,
        style = AppTypography.text,
        color = Color.LightGray
    )
}

@Composable
fun ShowParticipants(passengers: List<User>, freePlaces: Int) {
    val passengersWithFree = passengers + List(freePlaces) { null }
    LazyColumn {
        items(passengersWithFree) {
            ShowPassenger(it)
        }
    }
}

@Composable
fun ShowPassenger(user: User? = null, showContacts: Boolean = false) {
    Row(Modifier.padding(vertical = Spacing8dp), verticalAlignment = Alignment.CenterVertically) {
        AsyncImage(
            model = user?.imageUrl ?: "",
            contentDescription = stringResource(id = R.string.profile_pic),
            placeholder = painterResource(id = R.drawable.profile_avatar_placeholder),
            error = painterResource(id = R.drawable.profile_avatar_placeholder),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(Spacing32dp)
                .clip(CircleShape)
        )
        Spacer(Modifier.width(Spacing12dp))
        Text(text = user?.name ?: stringResource(R.string.free_place), style = AppTypography.text)
        if (showContacts) {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                Text(VK_USER_URL + (user?.id ?: "124124"), color = Blue)
            }
        }
    }
}

//@Preview
//@Composable
//fun showTrip() {
//    TripScreen(
//        Trip(
//            route = TripRoute(),
//            freePlaces = 3,
//            host = User("123", "ivan", "", UserContacts("123")),
//            passengers = listOf(),
//            taxiService = TaxiService.Yandex,
//            taxiVehicleClass = TaxiService.Yandex.YandexVehicleClass.Comfort
//        )
//    )
//}