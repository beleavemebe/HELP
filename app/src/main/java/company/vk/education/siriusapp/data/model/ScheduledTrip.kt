package company.vk.education.siriusapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ScheduledTrip(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "trip_id")
    val tripId: String,

    @ColumnInfo(name = "starts_at")
    val startsAt: Long,

    @ColumnInfo(name = "ends_at")
    val endsAt: Long,
)