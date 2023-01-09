package com.help.android.ui.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.help.android.R

const val MAIN_CHANNEL_ID = "CHANNEL_ID"

@RequiresApi(Build.VERSION_CODES.O)
fun Context.createMainNotificationChannel() {
    getSystemService(NotificationManager::class.java)
        .createNotificationChannel(
            NotificationChannel(
                MAIN_CHANNEL_ID,
                getString(R.string.general_notifications),
                NotificationManager.IMPORTANCE_DEFAULT
            )
        )
}