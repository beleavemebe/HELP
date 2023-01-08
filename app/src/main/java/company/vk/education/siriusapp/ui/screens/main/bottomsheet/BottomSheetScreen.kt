package company.vk.education.siriusapp.ui.screens.main.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import company.vk.education.siriusapp.R
import company.vk.education.siriusapp.domain.model.TaxiService
import company.vk.education.siriusapp.domain.model.TaxiVehicleClass
import company.vk.education.siriusapp.ui.library.trips.TripItem
import company.vk.education.siriusapp.ui.screens.main.LocalMainScreenIntentConsumer
import company.vk.education.siriusapp.ui.screens.main.MainScreenIntent
import company.vk.education.siriusapp.ui.library.trips.TripCard
import company.vk.education.siriusapp.ui.library.trips.TripItemButtonState
import company.vk.education.siriusapp.ui.theme.*
import company.vk.education.siriusapp.ui.utils.formatDate
import company.vk.education.siriusapp.ui.utils.formatTime

@Composable
fun BottomSheet(
    state: BottomSheetScreenState,
) {
    val intentConsumer = LocalMainScreenIntentConsumer.current
    Column {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(top = Spacing16dp), contentAlignment = Alignment.Center
        ) {
            Divider(
                Modifier
                    .fillMaxWidth(0.25f)
                    .clip(RoundedCornerShape(Spacing4dp)), color = Color.LightGray, thickness = 3.dp
            )
        }
        TripMainControls(
            state.startAddress,
            state.endAddress,
            formatDate(state.date),
            formatTime(state.date)
        )

        if (state.isSearchingTrips) {
            SearchTrips(state)
        } else {
            TripCreationControls(
                freePlaces = state.freePlaces,
                taxiService = state.taxiService,
                taxiVehicleClass = state.taxiVehicleClass,
                isShowingPickTaxiServiceMenu = state.isShowingPickTaxiServiceMenu,
                isShowingPickTaxiVehicleClassMenu = state.isShowingPickTaxiVehicleClassMenu,
            )
        }

        if (state.isShowingDatePicker) {
            DatePicker(
                prevDate = state.date, 
                onDatePicked = { 
                    intentConsumer.consume(MainScreenIntent.BottomSheetIntent.TripDatePicked(it))
                }
            )
        } else if (state.isShowingTimePicker) {
            require(state.date != null) {
                "Cannot set time when the date is null"
            }

            TimePicker(
                pickedDate = state.date, 
                onTimePicked = { 
                    intentConsumer.consume(MainScreenIntent.BottomSheetIntent.TripTimePicked(it))
                }
            )
        }
    }
}

@Composable
fun TripCreationControls(
    freePlaces: Int?,
    taxiService: TaxiService?,
    taxiVehicleClass: TaxiVehicleClass?,
    isShowingPickTaxiServiceMenu: Boolean,
    isShowingPickTaxiVehicleClassMenu: Boolean,
) {
    val intentConsumer = LocalMainScreenIntentConsumer.current
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
                        intentConsumer.consume(MainScreenIntent.BottomSheetIntent.SetFreePlacesAmount(amount))
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(Spacing16dp))

            val serviceMapper = LocalTaxiInfoMappers.current.taxiServiceMapper
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
                        intentConsumer.consume(MainScreenIntent.BottomSheetIntent.PickTaxiPreference(TaxiPreference.TAXI_SERVICE))
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Box(Modifier.padding(start = 48.dp)) {
                DropdownMenu(
                    expanded = isShowingPickTaxiServiceMenu,
                    onDismissRequest = {
                        intentConsumer.consume(MainScreenIntent.BottomSheetIntent.DismissTaxiPreferenceMenu(TaxiPreference.TAXI_SERVICE))
                    },
                    modifier = Modifier.background(White)
                ) {
                    Column {
                        TaxiService.SERVICES.forEach { service ->
                            val text = stringResource(id = serviceMapper.map(service))
                            DropdownMenuItem(
                                onClick = {
                                    intentConsumer.consume(MainScreenIntent.BottomSheetIntent.TaxiServicePicked(service))
                                }
                            ) {
                                Text(text, style = AppTypography.text)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(Spacing16dp))

            val vehicleClassMapper = LocalTaxiInfoMappers.current.taxiVehicleClassMapper
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
                        intentConsumer.consume(MainScreenIntent.BottomSheetIntent.PickTaxiPreference(TaxiPreference.TAXI_VEHICLE_CLASS))
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Box(Modifier.padding(start = 48.dp)) {
                DropdownMenu(
                    expanded = isShowingPickTaxiVehicleClassMenu,
                    onDismissRequest = {
                        intentConsumer.consume(MainScreenIntent.BottomSheetIntent.DismissTaxiPreferenceMenu(TaxiPreference.TAXI_VEHICLE_CLASS))
                    },
                    modifier = Modifier.background(White)
                ) {
                    Column {
                        taxiService?.classes?.forEach { vehicleClass ->
                            val text = stringResource(id = vehicleClassMapper.map(vehicleClass))
                            DropdownMenuItem(
                                onClick = {
                                    intentConsumer.consume(MainScreenIntent.BottomSheetIntent.TaxiVehicleClassPicked(vehicleClass))
                                }
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
                    onClick = {
                        intentConsumer.consume(MainScreenIntent.BottomSheetIntent.PublishTrip)
                    },
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
                    onClick = {
                        intentConsumer.consume(MainScreenIntent.BottomSheetIntent.CancelCreatingTrip)
                    },
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
    val service by remember { mutableStateOf<TaxiService?>(null) }
    val vehicleClass by remember { mutableStateOf<TaxiVehicleClass?>(null) }
    val isShowingPickTaxiServiceMenu by remember { mutableStateOf(false) }
    val isShowingPickTaxiVehicleClassMenu by remember { mutableStateOf(false) }
    TripCreationControls(
        freePlaces = 2,
        taxiService = service,
        taxiVehicleClass = vehicleClass,
        isShowingPickTaxiServiceMenu = isShowingPickTaxiServiceMenu,
        isShowingPickTaxiVehicleClassMenu = isShowingPickTaxiVehicleClassMenu,
    )
}


@Composable
fun SearchTrips(
    state: BottomSheetScreenState,
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
                TripsHeader(state.trips.size)
                Spacer(modifier = Modifier.height(Spacing4dp))
                Trips(trips = state.trips)
            }
        }
    }
}

@Composable
@Preview
fun TripsHeaderPreview() = AppTheme {
    TripsHeader(count = 5)
}

@Composable
fun TripsHeader(count: Int) {
    val intentConsumer = LocalMainScreenIntentConsumer.current
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
                modifier = Modifier.clickable {
                    intentConsumer.consume(MainScreenIntent.BottomSheetIntent.CreateTrip)
                }
            )
        }
    }
}

@Composable
fun Loading() {
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
            )
        }
    }
}

@Composable
fun TripMainControls(
    startAddress: String,
    endAddress: String,
    tripDate: String,
    tripTime: String
) {
    val intentConsumer = LocalMainScreenIntentConsumer.current
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
                readOnly = true,
                trailingIcon = {
                    TextFieldMapIcon {
                        intentConsumer.consume(MainScreenIntent.BottomSheetIntent.PickStartOnTheMap)
                    }
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
                readOnly = true,
                trailingIcon = {
                    TextFieldMapIcon {
                        intentConsumer.consume(MainScreenIntent.BottomSheetIntent.PickEndOnTheMap)
                    }
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
                    interactionSource = textFieldInteractionSource {
                        intentConsumer.consume(MainScreenIntent.BottomSheetIntent.PickTripDate)
                    }
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
                    interactionSource = textFieldInteractionSource {
                        intentConsumer.consume(MainScreenIntent.BottomSheetIntent.PickTripTime)
                    }
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

@Composable
fun NoTripsFound() {
    val intentConsumer = LocalMainScreenIntentConsumer.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.fillMaxHeight(0.3f))

        Text(text = stringResource(R.string.no_trips_found), color = TextHint)

        Spacer(modifier = Modifier.fillMaxHeight(0.2f))

        Button(
            onClick = {
                intentConsumer.consume(MainScreenIntent.BottomSheetIntent.CreateTrip)
            },
            colors = ButtonDefaults.buttonColors(Blue),
            shape = Shapes.medium,
            elevation = null,
            modifier = Modifier
                .wrapContentWidth()
                .height(TextFieldHeight)
        ) {
            Text(
                stringResource(R.string.create_a_trip),
                style = AppTypography.subheadMedium,
                color = OnBlue
            )
        }

        Spacer(modifier = Modifier.fillMaxHeight(0.3f))
    }
}

@Composable
fun Trips(
    trips: List<TripCard>,
) {
    val intentConsumer = LocalMainScreenIntentConsumer.current
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(top = Spacing4dp)
    ) {
        items(trips) { tripCard ->
            TripItem(
                tripCard = tripCard,
                onCardClicked = { trip ->
                    intentConsumer.consume(
                        MainScreenIntent.BottomSheetIntent.ShowTripDetails(trip)
                    )
                },
                onButtonClicked = { trip ->
                    when (tripCard.tripItemButtonState) {
                        TripItemButtonState.HOST, TripItemButtonState.BOOKED -> {
                            intentConsumer.consume(
                                MainScreenIntent.BottomSheetIntent.ShowTripDetails(trip)
                            )
                        }
                        TripItemButtonState.JOIN -> {
                            intentConsumer.consume(
                                MainScreenIntent.BottomSheetIntent.JoinTrip(trip)
                            )
                        }
                        else -> {}
                    }
                }
            )
        }
    }
}

