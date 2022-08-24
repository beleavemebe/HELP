package company.vk.education.siriusapp.domain.service

import android.app.AlarmManager
import android.content.Context
import androidx.work.*
import company.vk.education.siriusapp.data.worker.ClearCurrentTripWorker
import company.vk.education.siriusapp.data.worker.HandleCurrentTripWorker
import company.vk.education.siriusapp.data.worker.KEY_TRIP_ID
import company.vk.education.siriusapp.data.worker.SetCurrentTripWorker
import company.vk.education.siriusapp.domain.model.Trip
import company.vk.education.siriusapp.ui.utils.MINUTE_MS
import company.vk.education.siriusapp.ui.utils.log
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject

private const val SET_TO_CLEAR_DELAY = 30 * MINUTE_MS

class ScheduledTripsServiceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ScheduledTripsService {
    override fun scheduleTripAt(date: Date, trip: Trip) {
        scheduleSetCurrentTripWorker(trip, date)
        scheduleClearCurrentTripWorker(trip, date)
    }

    private fun scheduleSetCurrentTripWorker(trip: Trip, date: Date) =
        scheduleWorker<SetCurrentTripWorker>(trip, date.time)

    private fun scheduleClearCurrentTripWorker(trip: Trip, date: Date) =
        scheduleWorker<ClearCurrentTripWorker>(trip, date.time + SET_TO_CLEAR_DELAY)

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

    override fun isTripScheduledAt(date: Date): Boolean {
        return false
    }
}