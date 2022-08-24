package company.vk.education.siriusapp.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.WorkerParameters
import company.vk.education.siriusapp.domain.service.CurrentTripService
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class ClearCurrentTripWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParameters: WorkerParameters,
    private val currentTripService: CurrentTripService,
)  : HandleCurrentTripWorker(appContext, workerParameters, currentTripService) {
    override suspend fun handle(currentTripService: CurrentTripService, tripId: String) {
        currentTripService.clearCurrentTrip()
    }
}