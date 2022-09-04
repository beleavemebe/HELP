package company.vk.education.siriusapp.ui.library.trips

import androidx.compose.ui.graphics.Color
import company.vk.education.siriusapp.core.Mapper
import company.vk.education.siriusapp.ui.utils.unprovidedCompositionLocalOf

val LocalTripItemMappers = unprovidedCompositionLocalOf<TripItemMappers>()

interface TripItemMappers {
    val tripItemButtonTextMapper: Mapper<TripItemButtonState, Int>
    val tripItemButtonColorMapper: Mapper<TripItemButtonState, Color>
}