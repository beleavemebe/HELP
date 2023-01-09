package company.vk.education.siriusapp.ui.screens.trip

import company.vk.education.siriusapp.core.Mapper
import company.vk.education.siriusapp.ui.screens.trip.model.TripScreenTitle

interface TripScreenMappers {
    val tripScreenTitleMapper: Mapper<TripScreenTitle, Int>
}