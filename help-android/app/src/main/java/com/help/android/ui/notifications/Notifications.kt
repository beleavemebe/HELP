package com.help.android.ui.notifications

import android.content.Context
import androidx.core.app.NotificationCompat
import com.help.android.R


object Notifications {
    object Ids {
        const val UPDATING_CURRENT_TRIP = 1313
    }

    fun createUpdatingCurrentTripNotification(context: Context) =
        NotificationCompat.Builder(context, MAIN_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_trashcan)
            .setContentText(context.getString(R.string.updating_current_trip))
            .build()
}