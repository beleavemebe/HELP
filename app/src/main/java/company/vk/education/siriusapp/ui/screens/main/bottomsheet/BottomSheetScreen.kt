package company.vk.education.siriusapp.ui.screens.main.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import company.vk.education.siriusapp.R
import company.vk.education.siriusapp.domain.model.TaxiService
import company.vk.education.siriusapp.domain.model.TaxiVehicleClass
import company.vk.education.siriusapp.domain.model.Trip
import company.vk.education.siriusapp.ui.screens.main.HourAndMinute
import company.vk.education.siriusapp.ui.screens.main.MainScreenIntent
import company.vk.education.siriusapp.ui.screens.main.MainViewModel
import company.vk.education.siriusapp.ui.theme.*
import company.vk.education.siriusapp.ui.utils.LocalTaxiServiceToStringResMapper
import company.vk.education.siriusapp.ui.utils.LocalTaxiVehicleClassToStringResMapper
import company.vk.education.siriusapp.ui.utils.formatOrEmpty
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.text.SimpleDateFormat
import java.util.*

val MainViewModel.bottomSheetState
    get() = viewState
        .map { it.bottomSheetScreenState }
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            initialState.bottomSheetScreenState
        )

@Composable
fun BottomSheetScreen(
    viewModel: MainViewModel = viewModel()
) {
    val state by viewModel.bottomSheetState.collectAsState()
    BottomSheet(
        state = state,
        onPickStartOnTheMapClicked = { viewModel.accept(MainScreenIntent.BottomSheetIntent.PickStartOnTheMap) },
        onPickEndOnTheMapClicked = { viewModel.accept(MainScreenIntent.BottomSheetIntent.PickEndOnTheMap) },
        onDateClicked = { viewModel.accept(MainScreenIntent.BottomSheetIntent.PickTripDate) },
        onTimeClicked = { viewModel.accept(MainScreenIntent.BottomSheetIntent.PickTripTime) },
        onDatePicked = { viewModel.accept(MainScreenIntent.BottomSheetIntent.TripDatePicked(it)) },
        onTimePicked = { viewModel.accept(MainScreenIntent.BottomSheetIntent.TripTimePicked(it)) },
        onPickTaxiPreference = {
            viewModel.accept(
                MainScreenIntent.BottomSheetIntent.PickTaxiPreference(
                    it
                )
            )
        },
        onTaxiServicePicked = {
            viewModel.accept(
                MainScreenIntent.BottomSheetIntent.TaxiServicePicked(
                    it
                )
            )
        },
        onTaxiVehicleClassPicked = {
            viewModel.accept(
                MainScreenIntent.BottomSheetIntent.TaxiVehicleClassPicked(
                    it
                )
            )
        },
        onDismissPreferenceMenu = {
            viewModel.accept(
                MainScreenIntent.BottomSheetIntent.DismissTaxiPreferenceMenu(
                    it
                )
            )
        },
    )
}

@Composable
fun BottomSheet(
    state: BottomSheetScreenState,
    onPickStartOnTheMapClicked: () -> Unit,
    onPickEndOnTheMapClicked: () -> Unit,
    onDateClicked: () -> Unit,
    onTimeClicked: () -> Unit,
    onDatePicked: (Date?) -> Unit,
    onTimePicked: (HourAndMinute) -> Unit,
    onPickTaxiPreference: (TaxiPreference) -> Unit,
    onTaxiServicePicked: (TaxiService) -> Unit,
    onTaxiVehicleClassPicked: (TaxiVehicleClass) -> Unit,
    onDismissPreferenceMenu: (TaxiPreference) -> Unit
) {
    Column {
        TripMainControls(
            state.startAddress,
            state.endAddress,
            onPickStartOnTheMapClicked,
            onPickEndOnTheMapClicked,
            formatDate(state.date),
            formatTime(state.date),
            onDateClicked,
            onTimeClicked
        )

        if (state.isSearchingTrips) {
            SearchTrips(state)
        } else {
            CreateTrip(
                state,
                onPickTaxiPreference,
                onTaxiServicePicked,
                onTaxiVehicleClassPicked,
                onDismissPreferenceMenu
            )
        }

        if (state.isShowingDatePicker) {
            DatePicker(prevDate = state.date, onDatePicked = { onDatePicked(it) })
        } else if (state.isShowingTimePicker) {
            require(state.date != null) {
                "Cannot set time when the date is null"
            }

            TimePicker(pickedDate = state.date, onTimePicked = { onTimePicked(it) })
        }
    }
}

fun formatDate(date: Date?): String {
    return date.formatOrEmpty("d MMM")
}

fun formatTime(date: Date?): String {
    return date.formatOrEmpty("HH:mm")
}

@Composable
fun TripCreationControls(
    freePlaces: Int?,
    taxiService: TaxiService?,
    taxiVehicleClass: TaxiVehicleClass?,
    onPickTaxiPreference: (TaxiPreference) -> Unit,
    isShowingPickTaxiServiceMenu: Boolean,
    isShowingPickTaxiVehicleClassMenu: Boolean,
    onTaxiServicePicked: (TaxiService) -> Unit,
    onTaxiVehicleClassPicked: (TaxiVehicleClass) -> Unit,
    onDismissPreferenceMenu: (TaxiPreference) -> Unit
) {
    Column {
        Spacer(modifier = Modifier.height(Spacing16dp))

        var freePlacesAmount by remember { mutableStateOf(freePlaces) }
        IconAndTextField(
            iconPainter = painterResource(id = R.drawable.ic_user),
            iconDescription = stringResource(id = R.string.free_places),
        ) {
            VKUITextField(
                value = freePlacesAmount?.toString() ?: "",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                hint = stringResource(id = R.string.free_places),
                onValueChange = {
                    freePlacesAmount = runCatching {
                        it.toInt()
                    }.getOrDefault(0)
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(Spacing16dp))

        val serviceMapper = LocalTaxiServiceToStringResMapper.current
        val pickedTaxiService = taxiService?.let { stringResource(id = serviceMapper.map(it)) }
        IconAndTextField(
            iconPainter = painterResource(id = R.drawable.ic_car),
            iconDescription = stringResource(R.string.taxi_service),
        ) {
            VKUITextField(
                value = pickedTaxiService ?: "",
                hint = stringResource(R.string.taxi_service),
                onValueChange = { },
                readOnly = true,
                interactionSource = textFieldInteractionSource { onPickTaxiPreference(TaxiPreference.TAXI_SERVICE) },
                modifier = Modifier.fillMaxWidth()
            )
        }
        Box(Modifier.padding(start = 48.dp)) {
            DropdownMenu(
                expanded = isShowingPickTaxiServiceMenu,
                onDismissRequest = { onDismissPreferenceMenu(TaxiPreference.TAXI_SERVICE) },
                modifier = Modifier.background(Color.White)
            ) {
                Column {
                    TaxiService.SERVICES.forEach { service ->
                        val text = stringResource(id = serviceMapper.map(service))
                        DropdownMenuItem(
                            onClick = { onTaxiServicePicked(service) }
                        ) {
                            Text(text, style = AppTypography.text)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(Spacing16dp))

        val vehicleClassMapper = LocalTaxiVehicleClassToStringResMapper.current
        val pickedTaxiVehicleClass = taxiVehicleClass?.let {
            stringResource(id = vehicleClassMapper.map(it))
        }
        IconAndTextField(
            iconPainter = painterResource(id = R.drawable.ic_wheel),
            iconDescription = stringResource(R.string.vehicle_class),
        ) {
            VKUITextField(
                value = pickedTaxiVehicleClass ?: "",
                hint = stringResource(R.string.vehicle_class),
                onValueChange = { },
                readOnly = true,
                interactionSource = textFieldInteractionSource { onPickTaxiPreference(TaxiPreference.TAXI_VEHICLE_CLASS) },
                modifier = Modifier.fillMaxWidth()
            )
        }
        Box(Modifier.padding(start = 48.dp)) {
            DropdownMenu(
                expanded = isShowingPickTaxiVehicleClassMenu,
                onDismissRequest = { onDismissPreferenceMenu(TaxiPreference.TAXI_VEHICLE_CLASS) },
                modifier = Modifier.background(Color.White)
            ) {
                Column {
                    taxiService?.classes?.forEach { vehicleClass ->
                        val text = stringResource(id = vehicleClassMapper.map(vehicleClass))
                        DropdownMenuItem(
                            onClick = { onTaxiVehicleClassPicked(vehicleClass) }
                        ) {
                            Text(text, style = AppTypography.text)
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun TripCreationControlsPreview() = AppTheme {
    var service by remember { mutableStateOf<TaxiService?>(null) }
    var vehicleClass by remember { mutableStateOf<TaxiVehicleClass?>(null) }
    var isShowingPickTaxiServiceMenu by remember { mutableStateOf(false) }
    var isShowingPickTaxiVehicleClassMenu by remember { mutableStateOf(false) }
    TripCreationControls(
        freePlaces = 2,
        taxiService = service,
        taxiVehicleClass = vehicleClass,
        onPickTaxiPreference = {
            when (it) {
                TaxiPreference.TAXI_SERVICE -> isShowingPickTaxiServiceMenu = true
                TaxiPreference.TAXI_VEHICLE_CLASS -> isShowingPickTaxiVehicleClassMenu = true
            }
        },
        isShowingPickTaxiServiceMenu = isShowingPickTaxiServiceMenu,
        isShowingPickTaxiVehicleClassMenu = isShowingPickTaxiVehicleClassMenu,
        onTaxiServicePicked = {
            service = it
        },
        onTaxiVehicleClassPicked = {
            vehicleClass = it
        },
        onDismissPreferenceMenu = {
            when (it) {
                TaxiPreference.TAXI_SERVICE -> isShowingPickTaxiServiceMenu = false
                TaxiPreference.TAXI_VEHICLE_CLASS -> isShowingPickTaxiVehicleClassMenu = false
            }
        }
    )
}

@Composable
fun CreateTrip(
    state: BottomSheetScreenState,
    onPickTaxiPreference: (TaxiPreference) -> Unit,
    onTaxiServicePicked: (TaxiService) -> Unit,
    onTaxiVehicleClassPicked: (TaxiVehicleClass) -> Unit,
    onDismissPreferenceMenu: (TaxiPreference) -> Unit
) {
    Column(
        Modifier.padding(Spacing16dp)
    ) {
        TripCreationControls(
            freePlaces = state.freePlaces,
            taxiService = state.taxiService,
            taxiVehicleClass = state.taxiVehicleClass,
            onPickTaxiPreference = onPickTaxiPreference,
            isShowingPickTaxiServiceMenu = state.isShowingPickTaxiServiceMenu,
            isShowingPickTaxiVehicleClassMenu = state.isShowingPickTaxiVehicleClassMenu,
            onTaxiServicePicked = onTaxiServicePicked,
            onTaxiVehicleClassPicked = onTaxiVehicleClassPicked,
            onDismissPreferenceMenu = onDismissPreferenceMenu
        )
    }
}


@Composable
fun SearchTrips(
    state: BottomSheetScreenState,
) {
    Column(
        Modifier.padding(Spacing16dp)
    ) {
        if (state.areTripsLoading) {
            Loading()
        } else if (state.trips == null) {
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
private fun Loading() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = Blue)
    }
}

@Composable
fun FillTheForms() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.fill_the_forms),
            style = AppTypography.subhead.copy(color = TextHint)
        )
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
                tripDate = "",
                tripTime = "",
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
    tripDate: String,
    tripTime: String,
    onDateClicked: () -> Unit,
    onTimeClicked: () -> Unit
) {
    Column(
        Modifier.padding(Spacing16dp)
    ) {
        IconAndTextField(
            iconPainter = painterResource(id = R.drawable.ic_my_location),
            iconDescription = stringResource(id = R.string.my_location),
        ) {
            VKUITextField(
                value = startAddress,
                hint = stringResource(id = R.string.my_location),
                onValueChange = { /*tripStartLocation = it*/ },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    TextFieldMapIcon(onPickStartOnTheMapClicked)
                }
            )
        }

        Spacer(Modifier.height(Spacing16dp))

        IconAndTextField(
            iconPainter = painterResource(id = R.drawable.ic_location),
            iconDescription = stringResource(id = R.string.location),
        ) {
            VKUITextField(
                value = endAddress,
                hint = stringResource(id = R.string.location),
                onValueChange = { /*tripEndLocation = it*/ },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    TextFieldMapIcon(onPickEndOnTheMapClicked)
                }
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
                    value = tripDate,
                    hint = stringResource(id = R.string.date),
                    onValueChange = {},
                    readOnly = true,
                    interactionSource = textFieldInteractionSource(onDateClicked)
                )
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
                    value = tripTime,
                    hint = stringResource(id = R.string.time),
                    onValueChange = {},
                    readOnly = true,
                    interactionSource = textFieldInteractionSource(onTimeClicked)
                )
            }
        }
    }
}

@Composable
private fun textFieldInteractionSource(onClick: () -> Unit) =
    remember { MutableInteractionSource() }
        .also { interactionSource ->
            LaunchedEffect(interactionSource) {
                interactionSource.interactions.collect {
                    if (it is PressInteraction.Release) {
                        onClick()
                    }
                }
            }
        }

@Composable
private fun TextFieldMapIcon(
    onClick: () -> Unit,
) {
    Row {
        Text(
            text = stringResource(R.string.map),
            color = Blue,
            modifier = Modifier.clickable {
                onClick()
            }
        )
        Spacer(modifier = Modifier.width(Spacing6dp))
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
    LazyColumn(Modifier.fillMaxSize()) {
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

