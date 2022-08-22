package company.vk.education.siriusapp.ui.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import company.vk.education.siriusapp.R
import company.vk.education.siriusapp.domain.model.User
import company.vk.education.siriusapp.ui.theme.AppTypography
import company.vk.education.siriusapp.ui.theme.Blue
import company.vk.education.siriusapp.ui.theme.Spacing16dp
import company.vk.education.siriusapp.ui.theme.TextHint

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UserModalSheet(
    user: User,
    modalBottomSheetState: ModalBottomSheetState
) {
    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetContent = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    model = user.imageUrl,
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
                        .size(200.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.height(Spacing16dp))
                Text(text = user.name, style = AppTypography.title2Medium)
                Spacer(modifier = Modifier.height(Spacing16dp))
                Text(text = user.contact.phoneNumber, style = AppTypography.headlineMedium.copy(color = TextHint))
                Spacer(modifier = Modifier.height(Spacing16dp))
                Text(text = user.contact.tgLink ?: "t.me/ristired", style = AppTypography.headlineMedium.copy(color = Blue))
                Spacer(modifier = Modifier.height(Spacing16dp))
                Text(text = user.contact.vkLink ?: "vk.com/id185436777", style = AppTypography.headlineMedium.copy(color = Blue))
                Spacer(modifier = Modifier.height(Spacing16dp))
            }
        }
    ) {
    }
}