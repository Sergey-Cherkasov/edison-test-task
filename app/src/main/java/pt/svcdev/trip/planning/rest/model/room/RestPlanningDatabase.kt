package pt.svcdev.trip.planning.rest.model.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DatePlanningEntity::class, LocationWeatherEntity::class], version = 1)
abstract class RestPlanningDatabase: RoomDatabase() {
    abstract fun dao(): RestPlanningDao
}