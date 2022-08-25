package company.vk.education.siriusapp.ui.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import coil.compose.AsyncImage
import company.vk.education.siriusapp.R
import company.vk.education.siriusapp.ui.screens.main.bottomsheet.TripItem
import company.vk.education.siriusapp.ui.screens.main.trip.TripCard
import company.vk.education.siriusapp.ui.screens.main.trip.TripCardButtonState
import company.vk.education.siriusapp.ui.screens.main.user.UserState
import company.vk.education.siriusapp.ui.theme.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UserModalSheet(
    state: UserState,
    modalBottomSheetState: ModalBottomSheetState
) {
    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(32.dp, 32.dp, 0.dp, 0.dp),
        sheetContent = {
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
                                        .padding(top = Spacing16dp),
                                    contentAlignment = Alignment.Center
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
                                        text = stringResource(id = R.string.user),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        },
                    )

                    Spacer(modifier = Modifier.height(Spacing16dp))

                    UserHeader(state)

                    Spacer(modifier = Modifier.height(Spacing16dp))

                    if (state.currentTrip != null) {
                        Text(text = stringResource(id = R.string.current_trip), style = AppTypography.headline)
                        Spacer(modifier = Modifier.height(Spacing8dp))
                        TripItem(
                            tripCard = TripCard(
                                trip = state.currentTrip,
                                dist = 0,
                                isCurrentTrip = true,
                                tripCardButtonState = TripCardButtonState.INVISIBLE
                            ),
                            onTripClicked = {},
                            onJoinTripClicked = {}
                        )
                    }

                    if (state.scheduledTrips.isNotEmpty()) {
                        Text(text = stringResource(id = R.string.scheduled_trips), style = AppTypography.headline)
                        Spacer(modifier = Modifier.height(Spacing8dp))
                        state.scheduledTrips.forEach {
                            TripItem(
                                tripCard = TripCard(
                                    it,
                                    0,
                                    isCurrentTrip = false,
                                    TripCardButtonState.INVISIBLE
                                ),
                                onTripClicked = {},
                                onJoinTripClicked = {}
                            )
                        }

                        Spacer(modifier = Modifier.height(Spacing16dp))
                    }

                    if (state.previousTrips.isNotEmpty()) {
                        Text(text = stringResource(id = R.string.previous_trips), style = AppTypography.headline)
                        Spacer(modifier = Modifier.height(Spacing8dp))
                        state.previousTrips.forEach {
                            TripItem(
                                tripCard = TripCard(
                                    it,
                                    0,
                                    isCurrentTrip = false,
                                    TripCardButtonState.INVISIBLE
                                ),
                                onTripClicked = {},
                                onJoinTripClicked = {}
                            )
                        }

                        Spacer(modifier = Modifier.height(Spacing16dp))
                    }
                }
            }
        }
    ) {}
}

@Composable
private fun UserHeader(user: UserState) {
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        Column {
            Text(text = user.user.name, style = AppTypography.headlineMedium)

            Spacer(modifier = Modifier.height(Spacing4dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.ic_rating),
                    contentDescription = stringResource(R.string.rating),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(Spacing12dp))
                Text(
                    text = user.user.rating.toInt().toString() + "%",
                    style = AppTypography.subhead.copy(color = TextHint)
                )
            }

            Spacer(modifier = Modifier.height(Spacing4dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.ic_logo_vk),
                    contentDescription = stringResource(R.string.rating),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(Spacing12dp))
                Text(
                    text = "id${user.user.id}",
                    style = AppTypography.subhead.copy(color = TextHint)
                )
            }
        }

        Box(contentAlignment = Alignment.TopEnd) {
            Spacer(modifier = Modifier.fillMaxWidth())
            AsyncImage(
                model = user.user.imageUrl,
                contentDescription = stringResource(id = R.string.profile_pic),
                placeholder = painterResource(
                    id = R.drawable.profile_avatar_placeholder
                ),
                error = painterResource(
                    id = R.drawable.profile_avatar_placeholder
                ),
                contentScale = ContentScale.Crop,
                modifier = Modifier
//                    .padding(end = Spacing16dp)
                    .size(80.dp)
                    .clip(CircleShape)
            )
        }
    }
}

