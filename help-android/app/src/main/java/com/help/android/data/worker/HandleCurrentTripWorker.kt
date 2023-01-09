package com.help.android.data.worker

import android.app.Notification
import android.content.Context
import android.os.Build
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.help.android.domain.service.CurrentTripService
import com.help.android.ui.notifications.Notifications
import com.help.android.ui.notifications.createMainNotificationChannel

const val KEY_TRIP_ID = "keyTripId"

abstract class HandleCurrentTripWorker(
    appContext: Context,
    workerParameters: WorkerParameters,
    private val currentTripService: CurrentTripService,
)  : CoroutineWorker(appContext, workerParameters) {
    override suspend fun doWork(): Result {
        val tripId = inputData.getString(KEY_TRIP_ID)
            ?: return Result.failure(workDataOf("" to "Current trip id not found"))
        handle(currentTripService, tripId)
        return Result.success()
    }

    abstract suspend fun handle(currentTripService: CurrentTripService, tripId: String)

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(Notifications.Ids.UPDATING_CURRENT_TRIP, createNotification())
    }

    private fun createNotification(): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            applicationContext.createMainNotificationChannel()
        }

        return Notifications.createUpdatingCurrentTripNotification(applicationContext)
    }
}