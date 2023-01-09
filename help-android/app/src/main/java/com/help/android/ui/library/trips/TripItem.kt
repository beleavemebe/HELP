package com.help.android.ui.library.trips

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.help.android.domain.model.Trip
import com.help.android.R
import com.help.android.ui.library.trips.TripItemButtonState.*
import com.help.android.ui.screens.main.bottomsheet.LocalTaxiInfoMappers
import com.help.android.ui.utils.formatDate
import com.help.android.ui.utils.formatTime
import com.help.android.ui.theme.*

@Composable
fun TripItem(
    tripCard: TripCard,
    onCardClicked: (Trip) -> Unit,
    onButtonClicked: (Trip) -> Unit,
) {
    val trip = tripCard.trip
    val cardColor = if (tripCard.isCurrentTrip) Blue.copy(alpha = 0.9F) else Color.White
    val textColor = if (tripCard.isCurrentTrip) OnBlue else Black
    val textSecondaryColor = if (tripCard.isCurrentTrip) HintOnBlue else Grey
    Spacer(modifier = Modifier.height(Spacing4dp))
    Surface(
        shape = RoundedCornerShape(16.dp),
        elevation = 4.dp,
        color = cardColor,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onCardClicked(trip)
            }
    ) {
        Column(Modifier.padding(Spacing16dp)) {
            if (tripCard.tripItemButtonState == HOST) {
                Text(
                    stringResource(id = R.string.your_trip),
                    style = AppTypography.caption1,
                    color = textSecondaryColor
                )
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(Spacing32dp),
                Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val date = formatDate(trip.route.date)
                val time = formatTime(trip.route.date)
                Text(
                    stringResource(R.string.in_placeholder, date, time),
                    style = AppTypography.headline,
                    color = textColor
                )
                Row(horizontalArrangement = Arrangement.spacedBy((-8).dp)) {
                    (listOf(trip.host) + trip.passengers).forEachIndexed { i, user ->
                        AsyncImage(
                            model = user.imageUrl, contentDescription = "userPhoto",
                            placeholder = painterResource(id = R.drawable.profile_avatar_placeholder),
                            error = painterResource(id = R.drawable.profile_avatar_placeholder),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .border(1.dp, OnBlue, CircleShape)
                                .zIndex(5 - i.toFloat())
                        )
                    }
                }
            }

            val serviceMapper = LocalTaxiInfoMappers.current.taxiServiceMapper
            val vehicleClassMapper = LocalTaxiInfoMappers.current.taxiVehicleClassMapper
            val taxiService = stringResource(id = serviceMapper.map(trip.taxiService))
            val vehicleClass = stringResource(id = vehicleClassMapper.map(trip.taxiVehicleClass))
            Text(
                stringResource(
                    id = if (tripCard.dist != 0) R.string.trip_info else R.string.trip_info_no_distance,
                    taxiService,
                    vehicleClass,
                    tripCard.dist
                ),
                style = AppTypography.caption1,
                color = textSecondaryColor
            )
            if (tripCard.tripItemButtonState != INVISIBLE) {
                Spacer(modifier = Modifier.height(Spacing12dp))
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp),
                    shape = RoundedCornerShape(Spacing8dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = tripCard.tripItemButtonState.buttonColor
                    ),
                    elevation = null,
                    onClick = {
                        onButtonClicked(trip)
                    }
                ) {
                    Text(
                        stringResource(tripCard.tripItemButtonState.buttonTextRes),
                        style = AppTypography.caption2Medium,
                        color = OnBlue
                    )
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(Spacing12dp))
}