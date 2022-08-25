package company.vk.education.siriusapp.data

import android.app.AlarmManager
import android.content.Context
import androidx.work.*
import company.vk.education.siriusapp.data.worker.ClearCurrentTripWorker
import company.vk.education.siriusapp.data.worker.HandleCurrentTripWorker
import company.vk.education.siriusapp.data.worker.KEY_TRIP_ID
import company.vk.education.siriusapp.data.worker.SetCurrentTripWorker
import company.vk.education.siriusapp.domain.model.Trip
import company.vk.education.siriusapp.domain.repository.TripsRepository
import company.vk.education.siriusapp.domain.service.AuthService
import company.vk.education.siriusapp.domain.service.ScheduledTripsService
import company.vk.education.siriusapp.ui.utils.MINUTE_MS
import company.vk.education.siriusapp.ui.utils.log
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject

private const val SET_TO_CLEAR_DELAY = 30 * MINUTE_MS

class ScheduledTripsServiceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authService: AuthService,
    private val tripsRepository: TripsRepository,
) : ScheduledTripsService {
    override suspend fun scheduleTripAt(date: Date, trip: Trip) {
        val tripStartsAt = date.time
        val tripEndsAt = tripStartsAt + SET_TO_CLEAR_DELAY

        scheduleWorker<SetCurrentTripWorker>(trip, tripStartsAt)
        scheduleWorker<ClearCurrentTripWorker>(trip, tripEndsAt)

        tripsRepository.appendToTripHistory(
            userId = authService.authState.value.user!!.id,
            trip = trip
        )
    }

    private inline fun <reified Worker : HandleCurrentTripWorker> scheduleWorker(
        trip: Trip,
        time: Long
    ) {
        val listener = AlarmManager.OnAlarmListener {
            val workRequest = OneTimeWorkRequestBuilder<Worker>()
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .setConstraints(Constraints())
                .setInputData(Data.Builder().putString(KEY_TRIP_ID, trip.id).build())
                .build()

            WorkManager
                .getInstance(context)
                .enqueue(workRequest)
        }

        context.getSystemService(AlarmManager::class.java)
            .setExact(AlarmManager.RTC, time, "HelpAlarm", listener, null)

        log("Worker ${Worker::class.java.simpleName} scheduled at $time = ${Date(time)}")
    }

    override suspend fun isTripScheduledAt(date: Date): Boolean {
        return tripsRepository.getTripHistory(authService.authState.value.user!!.id)
            .any { date.time in it.route.date.time..(it.route.date.time + SET_TO_CLEAR_DELAY) }
    }
}