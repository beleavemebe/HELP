package com.help.android.data

import android.app.AlarmManager
import android.content.Context
import androidx.work.*
import com.help.android.data.worker.ClearCurrentTripWorker
import com.help.android.data.worker.HandleCurrentTripWorker
import com.help.android.data.worker.KEY_TRIP_ID
import com.help.android.data.worker.SetCurrentTripWorker
import com.help.android.domain.model.Trip
import com.help.android.domain.repository.TripsRepository
import com.help.android.domain.service.AuthService
import com.help.android.domain.service.ScheduledTripsService
import com.help.android.ui.utils.MINUTE_MS
import com.help.android.ui.utils.log
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
                .setConstraints(Constraints.Builder().build())
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