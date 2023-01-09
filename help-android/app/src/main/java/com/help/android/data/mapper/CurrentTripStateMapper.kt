package com.help.android.data.mapper

import com.help.android.CurrentTripStateMsg
import com.help.android.core.Mapper
import com.help.android.domain.model.CurrentTripState

class CurrentTripStateMapper : Mapper<CurrentTripStateMsg, CurrentTripState> {
    override fun map(arg: CurrentTripStateMsg): CurrentTripState {
        return CurrentTripState(isUnknown = arg.isUnknown, currentTripId = arg.tripId)
    }
}