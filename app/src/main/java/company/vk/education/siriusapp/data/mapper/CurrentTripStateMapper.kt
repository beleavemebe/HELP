package company.vk.education.siriusapp.data.mapper

import company.vk.education.siriusapp.CurrentTripStateMsg
import company.vk.education.siriusapp.core.Mapper
import company.vk.education.siriusapp.domain.model.CurrentTripState

class CurrentTripStateMapper : Mapper<CurrentTripStateMsg, CurrentTripState> {
    override fun map(arg: CurrentTripStateMsg): CurrentTripState {
        return CurrentTripState(isUnknown = arg.isUnknown, currentTripId = arg.tripId)
    }
}