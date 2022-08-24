package company.vk.education.siriusapp.ui.nofifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import company.vk.education.siriusapp.R

const val MAIN_CHANNEL_ID = "CHANNEL_ID"

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