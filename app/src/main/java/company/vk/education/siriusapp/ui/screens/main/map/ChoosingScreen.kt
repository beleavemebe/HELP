package company.vk.education.siriusapp.ui.screens.main.map

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.yandex.mapkit.mapview.MapView
import company.vk.education.siriusapp.R
import company.vk.education.siriusapp.ui.screens.main.MainViewState
import company.vk.education.siriusapp.ui.theme.AppTypography
import company.vk.education.siriusapp.ui.theme.Blue
import company.vk.education.siriusapp.ui.theme.OnBlue

@Composable
fun ChoosingScreen(state: MainViewState.MapViewState, map: MapView, onClick: () -> Unit) {
    if (state is MainViewState.MapViewState.ChoosingAddress) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Image(painterResource(id = R.drawable.pin), "pin", modifier = Modifier.size(64.dp).offset(0.dp, (-8).dp))
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)) {
            Text("Выберете адрес", style = AppTypography.title2)
            Text(state.currentlyChosenAddress, style = AppTypography.title1Bold)
            Spacer(modifier = Modifier.fillMaxHeight(0.88f))
            Button(onClick = onClick, modifier = Modifier
                .fillMaxWidth()
                .height(48.dp), colors = ButtonDefaults.buttonColors(Blue)) {
                Text("Выбрать местоположение", style = AppTypography.subheadMedium, color = OnBlue)
            }
        }
    }
}