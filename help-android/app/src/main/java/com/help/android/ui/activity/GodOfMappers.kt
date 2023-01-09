package com.help.android.ui.activity

import com.help.android.core.Mapper
import com.help.android.domain.model.TaxiService
import com.help.android.domain.model.TaxiVehicleClass
import com.help.android.ui.screens.main.bottomsheet.BottomSheetMappers
import com.help.android.ui.screens.trip.TripScreenMappers
import com.help.android.ui.screens.trip.model.TripScreenTitle
import com.help.android.ui.screens.user.UserScreenMappers
import javax.inject.Inject

class GodOfMappers @Inject constructor(
    override val taxiServiceMapper: Mapper<TaxiService, Int>,
    override val taxiVehicleClassMapper: Mapper<TaxiVehicleClass, Int>,
    override val tripScreenTitleMapper: Mapper<TripScreenTitle, Int>,
) : BottomSheetMappers, TripScreenMappers, UserScreenMappers