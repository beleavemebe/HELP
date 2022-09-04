package company.vk.education.siriusapp.ui.activity

import androidx.compose.ui.graphics.Color
import company.vk.education.siriusapp.core.Mapper
import company.vk.education.siriusapp.domain.model.TaxiService
import company.vk.education.siriusapp.domain.model.TaxiVehicleClass
import company.vk.education.siriusapp.ui.screens.main.bottomsheet.BottomSheetMappers
import company.vk.education.siriusapp.ui.screens.trip.TripScreenMappers
import company.vk.education.siriusapp.ui.library.trips.TripItemButtonState
import company.vk.education.siriusapp.ui.screens.trip.model.TripScreenTitle
import company.vk.education.siriusapp.ui.screens.user.UserScreenMappers
import javax.inject.Inject

class GodOfMappers @Inject constructor(
    override val taxiServiceMapper: Mapper<TaxiService, Int>,
    override val taxiVehicleClassMapper: Mapper<TaxiVehicleClass, Int>,
    override val tripScreenTitleMapper: Mapper<TripScreenTitle, Int>,
    override val tripItemButtonTextMapper: Mapper<TripItemButtonState, Int>,
    override val tripItemButtonColorMapper: Mapper<TripItemButtonState, Color>,
) : BottomSheetMappers, TripScreenMappers, UserScreenMappers