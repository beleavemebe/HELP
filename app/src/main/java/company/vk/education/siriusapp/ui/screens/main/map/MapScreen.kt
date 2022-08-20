package company.vk.education.siriusapp.ui.screens.main.map

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.yandex.mapkit.mapview.MapView
import company.vk.education.siriusapp.R
import company.vk.education.siriusapp.ui.screens.main.MainScreenIntent
import company.vk.education.siriusapp.ui.screens.main.MainScreenState
import company.vk.education.siriusapp.ui.screens.main.MainViewModel
import company.vk.education.siriusapp.ui.screens.main.MainViewState
import company.vk.education.siriusapp.ui.theme.Spacing16dp

@Composable
fun MapScreen(
    mapView: MapView,
    viewModel: MainViewModel = viewModel()
) = Map(
    mapView = mapView,
    state = viewModel.viewState.collectAsState()
) { viewModel.accept(MainScreenIntent.MapIntent.ShowProfile) }

@Composable
fun Map(mapView: MapView, state: State<MainScreenState>, onProfileClicked: () -> Unit) {
    Box(contentAlignment = Alignment.TopEnd) {
        AndroidView(factory = { mapView })
        ProfileView(state = state.value.mapState, onClick = onProfileClicked)
    }
}

@Composable
fun ProfileView(state: MainViewState.MapViewState, onClick: () -> Unit) {
    when (state) {
        is MainViewState.MapViewState.ChoosingAddress -> {}
        is MainViewState.MapViewState.Idle -> ProfileView(state.profilePicUrl, onClick)
    }
}

@Composable
fun ProfileView(url: String? = null, onClick: () -> Unit) {
    AsyncImage(
        model = url,
        contentDescription = stringResource(id = R.string.profile_pic),
        placeholder = painterResource(
            id = R.drawable.profile_avatar_placeholder
        ),
        error = painterResource(
            id = R.drawable.profile_avatar_placeholder
        ),
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .padding(top = Spacing16dp, end = Spacing16dp)
            .size(40.dp)
            .clip(CircleShape)
            .clickable { onClick() }
    )
}
