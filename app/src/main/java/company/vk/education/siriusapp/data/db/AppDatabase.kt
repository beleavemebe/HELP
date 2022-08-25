package company.vk.education.siriusapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import company.vk.education.siriusapp.data.model.ScheduledTrip

@Database(
    entities = [ScheduledTrip::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
//    abstract fun scheduledTripsDao(): ScheduledTripsDao

    companion object {
        const val DATABASE_NAME = "help.db"
    }
}