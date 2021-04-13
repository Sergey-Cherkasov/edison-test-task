package pt.svcdev.trip.planning.rest.model.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "location_weather",
//    foreignKeys = [
//        ForeignKey(
//            entity = DatePlanningEntity::class,
//            parentColumns = ["dateId"],
//            childColumns = ["dateOwnerId"]
//        )
//    ]
)
data class LocationWeatherEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val locationName: String,
    val temp: Double,
    val pressure: Int,
    val humidity: Int,
    val wind: Double,
    val dateOwnerId: Long
)