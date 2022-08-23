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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import company.vk.education.siriusapp.R
import company.vk.education.siriusapp.domain.model.*
import company.vk.education.siriusapp.ui.screens.main.HourAndMinute
import company.vk.education.siriusapp.ui.screens.main.MainScreenIntent
import company.vk.education.siriusapp.ui.screens.main.MainViewModel
import company.vk.education.siriusapp.ui.theme.*
import company.vk.education.siriusapp.ui.utils.LocalTaxiServiceToStringResMapper
import company.vk.education.siriusapp.ui.utils.LocalTaxiVehicleClassToStringResMapper
import company.vk.education.siriusapp.ui.utils.formatOrEmpty
import company.vk.education.siriusapp.ui.utils.log
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
        onPickStartOnTheMapClicked = {
            viewModel.accept(MainScreenIntent.BottomSheetIntent.PickStartOnTheMap)
        },
        onPickEndOnTheMapClicked = {
            viewModel.accept(MainScreenIntent.BottomSheetIntent.PickEndOnTheMap)
        },
        onDateClicked = {
            viewModel.accept(MainScreenIntent.BottomSheetIntent.PickTripDate)
        },
        onTimeClicked = {
            viewModel.accept(MainScreenIntent.BottomSheetIntent.PickTripTime)
        },
        onDatePicked = {
            viewModel.accept(MainScreenIntent.BottomSheetIntent.TripDatePicked(it))
        },
        onTimePicked = {
            viewModel.accept(MainScreenIntent.BottomSheetIntent.TripTimePicked(it))
        },
        onPickTaxiPreference = {
            viewModel.accept(MainScreenIntent.BottomSheetIntent.PickTaxiPreference(it))
        },
        onTaxiServicePicked = {
            viewModel.accept(MainScreenIntent.BottomSheetIntent.TaxiServicePicked(it))
        },
        onTaxiVehicleClassPicked = {
            viewModel.accept(MainScreenIntent.BottomSheetIntent.TaxiVehicleClassPicked(it))
        },
        onDismissPreferenceMenu = {
            viewModel.accept(MainScreenIntent.BottomSheetIntent.DismissTaxiPreferenceMenu(it))
        },
        onCreateTripClicked = {
            viewModel.accept(MainScreenIntent.BottomSheetIntent.CreateTrip)
        },
        onFreePlacesAmountChanged = {
            viewModel.accept(MainScreenIntent.BottomSheetIntent.SetFreePlacesAmount(it))
        },
        onPublishTripClicked = {
            viewModel.accept(MainScreenIntent.BottomSheetIntent.PublishTrip)
        },
        onCancelClicked = {
            viewModel.accept(MainScreenIntent.BottomSheetIntent.CancelCreatingTrip)
        },
        onTripClicked = {
            viewModel.accept(MainScreenIntent.ShowTripDetails(it))
        },
        onJoinTripClicked = {
            viewModel.accept(MainScreenIntent.BottomSheetIntent.JoinTrip(it))
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
    onDismissPreferenceMenu: (TaxiPreference) -> Unit,
    onCreateTripClicked: () -> Unit,
    onFreePlacesAmountChanged: (Int) -> Unit,
    onPublishTripClicked: () -> Unit,
    onCancelClicked: () -> Unit,
    onTripClicked: (Trip) -> Unit,
    onJoinTripClicked: (Trip) -> Unit,
) {
    Column {
        Box(Modifier.fillMaxWidth().padding(top = Spacing16dp), contentAlignment = Alignment.Center) {
            Divider(Modifier.fillMaxWidth(0.25f).clip(RoundedCornerShape(Spacing4dp)), color = Color.LightGray, thickness = 3.dp)
        }
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
            SearchTrips(
                state,
                onCreateTripClicked,
                onTripClicked,
                onJoinTripClicked,
            )
        } else {
            CreateTrip(
                state,
                onPickTaxiPreference,
                onTaxiServicePicked,
                onTaxiVehicleClassPicked,
                onDismissPreferenceMenu,
                onFreePlacesAmountChanged,
                onPublishTripClicked,
                onCancelClicked
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
    onDismissPreferenceMenu: (TaxiPreference) -> Unit,
    onFreePlacesAmountChanged: (Int) -> Unit,
    onPublishTripClicked: () -> Unit,
    onCancelClicked: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            Modifier.padding(
                start = Spacing16dp,
                end = Spacing16dp,
            )
        ) {
            var freePlacesAmount by rememberSaveable { mutableStateOf(freePlaces) }
            IconAndTextField(
                iconPainter = painterResource(id = R.drawable.ic_user),
                iconDescription = stringResource(id = R.string.free_places),
            ) {
                VKUITextField(
                    value = freePlacesAmount?.toString() ?: "",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    hint = stringResource(id = R.string.free_places),
                    onValueChange = {
                        val amount = runCatching {
                            it.toInt()
                        }.getOrDefault(0)
                        freePlacesAmount = amount
                        onFreePlacesAmountChanged(amount)
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
                    interactionSource = textFieldInteractionSource {
                        onPickTaxiPreference(
                            TaxiPreference.TAXI_SERVICE
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Box(Modifier.padding(start = 48.dp)) {
                DropdownMenu(
                    expanded = isShowingPickTaxiServiceMenu,
                    onDismissRequest = { onDismissPreferenceMenu(TaxiPreference.TAXI_SERVICE) },
                    modifier = Modifier.background(White)
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
                    interactionSource = textFieldInteractionSource {
                        onPickTaxiPreference(
                            TaxiPreference.TAXI_VEHICLE_CLASS
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Box(Modifier.padding(start = 48.dp)) {
                DropdownMenu(
                    expanded = isShowingPickTaxiVehicleClassMenu,
                    onDismissRequest = { onDismissPreferenceMenu(TaxiPreference.TAXI_VEHICLE_CLASS) },
                    modifier = Modifier.background(White)
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

            Spacer(modifier = Modifier.fillMaxHeight())
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Spacing16dp, vertical = 0.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column {
                Button(
                    onClick = { onPublishTripClicked() },
                    colors = ButtonDefaults.buttonColors(Blue),
                    shape = Shapes.medium,
                    elevation = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(FabSize)
                ) {
                    Text(
                        stringResource(R.string.create_a_trip),
                        style = AppTypography.subheadMedium,
                        color = OnBlue
                    )
                }

                Spacer(modifier = Modifier.height(Spacing16dp))

                Button(
                    onClick = { onCancelClicked() },
                    colors = ButtonDefaults.buttonColors(OnBlue),
                    shape = Shapes.medium,
                    elevation = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(FabSize)
                ) {
                    Text(
                        stringResource(R.string.go_back_to_search),
                        style = AppTypography.subheadMedium,
                        color = Blue
                    )
                }
                Spacer(modifier = Modifier.height(Spacing16dp))
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
        },
        onFreePlacesAmountChanged = {},
        onPublishTripClicked = {},
        onCancelClicked = {},
    )
}

@Composable
fun CreateTrip(
    state: BottomSheetScreenState,
    onPickTaxiPreference: (TaxiPreference) -> Unit,
    onTaxiServicePicked: (TaxiService) -> Unit,
    onTaxiVehicleClassPicked: (TaxiVehicleClass) -> Unit,
    onDismissPreferenceMenu: (TaxiPreference) -> Unit,
    onFreePlacesAmountChanged: (Int) -> Unit,
    onPublishTripClicked: () -> Unit,
    onCancelClicked: () -> Unit,
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
        onDismissPreferenceMenu = onDismissPreferenceMenu,
        onFreePlacesAmountChanged = onFreePlacesAmountChanged,
        onPublishTripClicked = onPublishTripClicked,
        onCancelClicked = onCancelClicked,
    )
}


@Composable
fun SearchTrips(
    state: BottomSheetScreenState,
    onCreateTripClicked: () -> Unit,
    onTripClicked: (Trip) -> Unit,
    onJoinTripClicked: (Trip) -> Unit,
) {
    Column(
        Modifier.padding(horizontal = Spacing16dp)
    ) {
        if (state.areTripsLoading) {
            Loading()
        } else if (state.trips == null) {
            FillTheForms()
        } else {
            if (state.trips.isEmpty()) {
                NoTripsFound()
            } else {
                TripsHeader(state.trips.size, onCreateTripClicked)
                Trips(trips = state.trips, onTripClicked, onJoinTripClicked)
            }
        }
    }
}

@Composable
@Preview
fun TripsHeaderPreview() = AppTheme {
    TripsHeader(count = 5) {}
}

@Composable
fun TripsHeader(count: Int, onCreateTripClicked: () -> Unit) {
    Box(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.trips_found_placeholder, count),
            style = AppTypography.subheadMedium,
        )

        Box(contentAlignment = Alignment.TopEnd) {
            Spacer(modifier = Modifier.fillMaxWidth())
            Text(
                text = stringResource(R.string.create),
                color = Blue,
                style = AppTypography.subhead,
                modifier = Modifier.clickable { onCreateTripClicked() }
            )
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
        Box(modifier = Modifier.padding(horizontal = Spacing16dp)) {
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
fun Trips(
    trips: List<Trip>,
    onTripClicked: (Trip) -> Unit,
    onJoinTripClicked: (Trip) -> Unit,
) {
    LazyColumn(Modifier.fillMaxSize().padding(top = Spacing4dp)) {
        items(trips) {
            TripItem(it, onTripClicked, onJoinTripClicked)
        }
    }
}

@Preview
@Composable
fun TripItemPreview() {
    TripItem(
        trip = Trip(
            route = TripRoute(),
            freePlaces = 3,
            host = User("123", "ivan", "", UserContacts("123")),
            passengers = listOf(),
            taxiService = TaxiService.Yandex,
            taxiVehicleClass = TaxiService.Yandex.YandexVehicleClass.Comfort
        ),
        onTripClicked = {},
        onJoinTripClicked = {},
    )
}

@Composable
fun TripItem(
    trip: Trip,
    onTripClicked: (Trip) -> Unit,
    onJoinTripClicked: (Trip) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        elevation = 4.dp,
        modifier = Modifier.fillMaxWidth().clickable { onTripClicked(trip) }
    ) {
        Column(Modifier.padding(Spacing16dp)) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(Spacing32dp),
                Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    SimpleDateFormat(
                        "dd.MM в HH:mm",
                        Locale.getDefault()
                    ).format(trip.route.date),
                    style = AppTypography.headline,
                )
                ParticipantsRow {
                    var offset = 0
                    trip.passengers.forEachIndexed { i, url ->
                        AsyncImage(
                            model = url, contentDescription = "userPhoto",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .border(2.dp, OnBlue, CircleShape)
                                .zIndex(5 - i.toFloat())
                        )
                        println(offset)
                        offset += 26
                    }
                }
            }

            val serviceMapper = LocalTaxiServiceToStringResMapper.current
            val vehicleClassMapper = LocalTaxiVehicleClassToStringResMapper.current
            val taxiService = stringResource(id = serviceMapper.map(trip.taxiService))
            val vehicleClass = stringResource(id = vehicleClassMapper.map(trip.taxiVehicleClass))
            Text(
                "$taxiService · $vehicleClass · 200m away",
                style = AppTypography.caption1,
                color = Color.LightGray
            )
            Spacer(modifier = Modifier.height(Spacing12dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp),
                shape = RoundedCornerShape(Spacing8dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Blue),
                elevation = null,
                onClick = {
                    onJoinTripClicked(trip)
                    log("Присоединяюсь")
                }
            ) {
                Text(
                    stringResource(id = R.string.join),
                    style = AppTypography.caption2Medium,
                    color = OnBlue
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(Spacing16dp))
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

