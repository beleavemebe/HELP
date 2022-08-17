package company.vk.education.siriusapp.ui.screens.home

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.yandex.mapkit.mapview.MapView
import company.vk.education.siriusapp.domain.model.Trip
import company.vk.education.siriusapp.ui.theme.Blue900
import company.vk.education.siriusapp.ui.theme.AppTheme
import company.vk.education.siriusapp.ui.theme.Typography
import java.text.SimpleDateFormat


@Composable
@OptIn(ExperimentalMaterialApi::class)
fun HomeScreen(
    mapView: MapView,
    state: State<HomeViewState>
) {
    BottomSheetScaffold(
        sheetContent = {
            Text(text = state.toString())
            Trips(state.value.trips)
        }
    ) {
        AndroidView(factory = { mapView })
    }
}

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

@Composable
fun ParticipantsRow(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        // Don't constrain child views further, measure them with given constraints
        // List of measured children
        val placeables = measurables.map { measurable ->
            // Measure each children
            measurable.measure(constraints)
        }

        // Set the size of the layout as big as it can
        layout(constraints.maxWidth, constraints.maxHeight) {
            // Track the y co-ord we have placed children up to
            var xPosition = 0

            // Place children in the parent layout
            placeables.forEach { placeable ->
                // Position item on the screen
                placeable.placeRelative(x = xPosition, y = 0)

                // Record the y co-ord placed up to
                xPosition += placeable.width / 3 * 2
            }
        }
    }
}

@Composable
fun TripItem(trip: Trip) {
    Row(Modifier.padding(16.dp)) {
        Column {
            Text("Поездка ${trip.id}", style = Typography.h3)
            Row(
                Modifier.fillMaxWidth(),
                Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(verticalArrangement = Arrangement.Bottom) {
                    Text("от ${trip.route.startLocation}", style = Typography.body1)
                    Text("до ${trip.route.endLocation}", style = Typography.body1)
                }
                Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("старт в", style = Typography.body2)
                    Text(
                        SimpleDateFormat("HH:mm").format(trip.route.date),
                        style = Typography.h2,
                        color = Blue900
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(modifier = Modifier
                .fillMaxWidth()
                .height(32.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Blue900),
                onClick = {
                    println("Присоединяюсь")
                }) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(Modifier.fillMaxWidth(0.2f))
                    Text("Присоединиться", fontSize = 12.sp, color = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    ParticipantsRow {
                        var offset = 0
                        trip.passengers.forEachIndexed { i, url ->
                            //Spacer(modifier = Modifier.size(4.dp))
                            AsyncImage(
                                model = url, contentDescription = "userPhoto",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(16.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, Blue900, CircleShape)
                                    //.offset(offset.dp, 0.dp)
                                    .zIndex(5 - i.toFloat())
                                //.rightPhoto(i.toFloat())
                            )
                            println(offset)
                            offset += 26
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "${trip.freePlaces} мест свободно",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                color = Color.LightGray
            )
        }
    }
}

@Preview
@Composable
fun PreviewTrip() {
    AppTheme {
//        TripItem(trip = Trip(route = TripRoute(), taxiServiceInfo = TaxiService.Yandex))
    }
}

@Composable
fun Trips(trips: List<Trip>) {
    LazyColumn {
        items(trips) {
            TripItem(it)
        }
    }
}