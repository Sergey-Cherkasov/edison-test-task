package pt.svcdev.trip.planning.rest.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "date_planning")
data class DatePlanningEntity(
    @PrimaryKey(autoGenerate = true) val dateId: Long,
    val dateStart: String,
    val dateEnd: String
)