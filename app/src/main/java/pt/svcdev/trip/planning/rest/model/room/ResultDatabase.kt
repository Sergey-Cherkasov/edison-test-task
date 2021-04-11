package pt.svcdev.trip.planning.rest.model.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ResultEntity::class, LocationEntity::class], version = 1, exportSchema = false)
abstract class ResultDatabase : RoomDatabase() {
    abstract fun resultDao(): ResultDao
}