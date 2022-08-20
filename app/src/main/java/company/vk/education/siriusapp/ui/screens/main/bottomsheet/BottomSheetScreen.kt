package company.vk.education.siriusapp.ui.screens.main.bottomsheet

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import company.vk.education.siriusapp.R
import company.vk.education.siriusapp.domain.model.TaxiService
import company.vk.education.siriusapp.domain.model.Trip
import company.vk.education.siriusapp.ui.screens.main.MainScreenIntent
import company.vk.education.siriusapp.ui.screens.main.MainScreenState
import company.vk.education.siriusapp.ui.screens.main.MainViewModel
import company.vk.education.siriusapp.ui.screens.main.MainViewState
import company.vk.education.siriusapp.ui.theme.*
import java.text.SimpleDateFormat

@Composable
fun BottomSheetScreen(
    viewModel: MainViewModel = viewModel()
) = BottomSheet(
    viewModel.viewState.collectAsState(),
    onPickStartOnTheMapClicked = {
        Log.d("ViewModel", "pickTripStart")
        viewModel.accept(MainScreenIntent.BottomSheetIntent.PickStartOnTheMap) },
    onPickEndOnTheMapClicked = {
        Log.d("ViewModel", "pickTripEnd")
        viewModel.accept(MainScreenIntent.BottomSheetIntent.PickEndOnTheMap) },
    onDateClicked = { viewModel.accept(MainScreenIntent.BottomSheetIntent.PickTripDate) },
    onTimeClicked = { viewModel.accept(MainScreenIntent.BottomSheetIntent.PickTripTime) }
)

@Composable
fun BottomSheet(
    state: State<MainScreenState>,
    onPickStartOnTheMapClicked: () -> Unit,
    onPickEndOnTheMapClicked: () -> Unit,
    onDateClicked: () -> Unit,
    onTimeClicked: () -> Unit,
) {
    when (val stateValue = state.value.bottomSheetState) {
        is MainViewState.BottomSheetState.SearchTrips -> {
            SearchTrips(stateValue, onPickStartOnTheMapClicked, onPickEndOnTheMapClicked, onDateClicked, onTimeClicked)
        }
        is MainViewState.BottomSheetState.CreateTrip -> {
            CreateTrip(stateValue, onPickStartOnTheMapClicked, onPickEndOnTheMapClicked, onDateClicked, onTimeClicked)
        }
    }
}


@Composable
fun TripCreationControls(freePlaces: Int, taxiService: TaxiService) {
    var freePlacesAmount by remember { mutableStateOf(freePlaces) }
    IconAndTextField(
        iconPainter = painterResource(id = R.drawable.ic_user),
        iconDescription = stringResource(id = R.string.free_places),
    ) {
        VKUITextField(
            value = freePlacesAmount.toString(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            hint = stringResource(id = R.string.free_places),
            onValueChange = { freePlacesAmount = it.toInt() },
            modifier = Modifier.fillMaxWidth()
        )
    }

    var pickedTaxiService by remember { mutableStateOf(taxiService) }
    IconAndTextField(
        iconPainter = painterResource(id = R.drawable.ic_user),
        iconDescription = stringResource(id = R.string.free_places),
    ) {
        VKUITextField(
            value = freePlacesAmount.toString(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            hint = stringResource(id = R.string.free_places),
            onValueChange = { freePlacesAmount = it.toInt() },
            modifier = Modifier.fillMaxWidth()
        )
    }

}

@Composable
@Preview
fun TripCreationControlsPreview() = AppTheme {
    TripCreationControls(freePlaces = 2, taxiService = TaxiService.Yandex)
}

@Composable
fun CreateTrip(
    state: MainViewState.BottomSheetState.CreateTrip,
    onPickStartOnTheMapClicked: () -> Unit,
    onPickEndOnTheMapClicked: () -> Unit,
    onDateClicked: () -> Unit,
    onTimeClicked: () -> Unit
) {
    Column(
        Modifier.padding(Spacing16dp)
    ) {
        TripMainControls(state.startAddress, state.endAddress, onPickStartOnTheMapClicked, onPickEndOnTheMapClicked, onDateClicked, onTimeClicked)
        TripCreationControls(state.freePlaces, state.taxiService)
    }
}


@Composable
fun SearchTrips(
    state: MainViewState.BottomSheetState.SearchTrips,
    onPickStartOnTheMapClicked: () -> Unit,
    onPickEndOnTheMapClicked: () -> Unit,
    onDateClicked: () -> Unit,
    onTimeClicked: () -> Unit,
) {
    Column(
        Modifier.padding(Spacing16dp)
    ) {
        TripMainControls(state.startAddress, state.endAddress, onPickStartOnTheMapClicked, onPickEndOnTheMapClicked, onDateClicked, onTimeClicked)
        if (state.trips == null) {
            FillTheForms()
        } else {
            if (state.trips.isEmpty()) {
                NoTripsFound()
            } else {
                Trips(trips = state.trips)
            }
        }
    }
}

@Composable
fun FillTheForms() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = stringResource(R.string.fill_the_forms))
    }
}

@Composable
@Preview
fun TripControlsPreview() {
    AppTheme {
        Box(modifier = Modifier.padding(Spacing16dp)) {
            TripMainControls(
                startAddress = "",
                endAddress = "",
                onDateClicked = {},
                onTimeClicked = {},
                onPickEndOnTheMapClicked = {},
                onPickStartOnTheMapClicked = {},
            )
        }
    }
}

@Composable
fun TripMainControls(
    startAddress: String,
    endAddress: String,
    onPickStartOnTheMapClicked: () -> Unit,
    onPickEndOnTheMapClicked: () -> Unit,
    onDateClicked: () -> Unit,
    onTimeClicked: () -> Unit
) {
    Column {
        var tripStartLocation by remember { mutableStateOf(startAddress) }
        IconAndTextField(
            iconPainter = painterResource(id = R.drawable.ic_my_location),
            iconDescription = stringResource(id = R.string.my_location),
            onIconClicked = onPickStartOnTheMapClicked,
        ) {
            VKUITextField(
                value = tripStartLocation,
                hint = stringResource(id = R.string.my_location),
                onValueChange = { tripStartLocation = it },
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(Modifier.height(Spacing16dp))
        IconAndTextField(
            iconPainter = painterResource(id = R.drawable.ic_location),
            iconDescription = stringResource(id = R.string.location),
            onIconClicked = onPickEndOnTheMapClicked
        ) {
            var tripEndLocation by remember { mutableStateOf(endAddress) }
            VKUITextField(
                value = tripEndLocation,
                hint = stringResource(id = R.string.location),
                onValueChange = { tripEndLocation = it },
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(Modifier.height(Spacing16dp))
        Row {
            IconAndTextField(
                iconPainter = painterResource(id = R.drawable.ic_calendar),
                iconDescription = stringResource(R.string.calendar),
                modifier = { fillMaxWidth(0.5f) }
            ) {
                VKUITextField(
                    value = "Today",
                    hint = stringResource(id = R.string.date),
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.clickable { onDateClicked() })
                Spacer(modifier = Modifier.width(Spacing8dp))
            }
            IconAndTextField(
                iconPainter = painterResource(id = R.drawable.ic_clock),
                iconDescription = stringResource(R.string.time),
                before = {
                    Spacer(modifier = Modifier.width(Spacing8dp))
                }
            ) {
                VKUITextField(
                    value = "13:13",
                    hint = stringResource(id = R.string.time),
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.clickable { onTimeClicked() })
            }
        }
    }
}

@Preview
@Composable
fun NoTripsFound() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.fillMaxHeight(0.3f))

        Text(text = stringResource(R.string.no_trips_found))

        Spacer(modifier = Modifier.fillMaxHeight(0.2f))

        Text(text = stringResource(R.string.create_a_trip) + " (todo)")

        Spacer(modifier = Modifier.fillMaxHeight(0.3f))
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

@Composable
fun TripItem(trip: Trip) {
    Row(Modifier.padding(16.dp)) {
        Column {
            Text("Поездка ${trip.id}", style = AppTypography.Typography.h3)
            Row(
                Modifier.fillMaxWidth(),
                Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(verticalArrangement = Arrangement.Bottom) {
                    Text("от ${trip.route.startLocation}", style = AppTypography.Typography.body1)
                    Text("до ${trip.route.endLocation}", style = AppTypography.Typography.body1)
                }
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("старт в", style = AppTypography.Typography.body2)
                    Text(
                        SimpleDateFormat("HH:mm").format(trip.route.date),
                        style = AppTypography.Typography.h2,
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

