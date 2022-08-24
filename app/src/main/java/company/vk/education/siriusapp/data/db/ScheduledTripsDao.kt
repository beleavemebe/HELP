package company.vk.education.siriusapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import company.vk.education.siriusapp.data.model.ScheduledTrip

@Dao
interface ScheduledTripsDao {
    @Insert
    suspend fun insertScheduledTrip(scheduledTrip: ScheduledTrip)

    @Query("SELECT * FROM scheduledtrip WHERE starts_at < (:at) AND (:at) < ends_at")
    suspend fun getScheduledTrips(at: Long): List<ScheduledTrip>
}