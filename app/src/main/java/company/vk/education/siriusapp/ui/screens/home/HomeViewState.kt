package company.vk.education.siriusapp.ui.screens.home

import company.vk.education.siriusapp.domain.model.TaxiService
import company.vk.education.siriusapp.domain.model.Trip
import company.vk.education.siriusapp.domain.model.TripRoute
import company.vk.education.siriusapp.ui.base.BaseViewState

data class HomeViewState(
    val route: TripRoute = TripRoute(),
    val selectedTab: BottomSheetTab = BottomSheetTab.FIND_TRIP,
    val isContentLoading: Boolean = true,
    val isError: Boolean = false,
    val trips: List<Trip> = emptyList(),
//    val price: Int? = null,
    val freePlaces: Int? = null,
    val taxiServiceInfo: TaxiService? = null,
    val verifyInstantly: Boolean = false,
) : BaseViewState