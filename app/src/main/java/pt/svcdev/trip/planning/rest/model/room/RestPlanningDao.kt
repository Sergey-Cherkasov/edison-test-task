package pt.svcdev.trip.planning.rest.model.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Transaction

@Dao
interface RestPlanningDao {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg model: DatePlaningWithLocationWeather) {
        insertDates(model[0].datePlanningEntity)
        model[0].locationWeatherEntity.forEach {
            insertLocationsWeather(it)
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDates(vararg date: DatePlanningEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocationsWeather(vararg locationsWeather: LocationWeatherEntity)
}