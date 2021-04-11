package pt.svcdev.trip.planning.rest.model.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Transaction

@Dao
interface ResultDao {
    // Сохранить данные
    // onConflict = OnConflictStrategy.IGNORE означает, что дубликаты не будут
    // сохраняться
    @Transaction
    fun insert(model: ResultModel) {
        insert(model.result)
        model.locations.forEach { location -> insert(location) }
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(data: ResultEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(data: LocationEntity)
}