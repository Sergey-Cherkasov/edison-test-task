package pt.svcdev.trip.planning.rest.model.room

import androidx.room.Embedded
import androidx.room.Relation

data class DatePlaningWithLocationWeather(
    @Embedded val datePlanningEntity: DatePlanningEntity,
    @Relation(
        parentColumn = "dateId",
        entityColumn = "dateOwnerId"
    )
    val locationWeatherEntity: List<LocationWeatherEntity>
)