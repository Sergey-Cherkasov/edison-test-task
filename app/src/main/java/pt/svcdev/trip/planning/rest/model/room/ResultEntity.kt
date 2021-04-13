package pt.svcdev.trip.planning.rest.model.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ResultEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "result_id") val id: Long,

    @ColumnInfo(name = "date_start") val dateStart: String,

    @ColumnInfo(name = "date_end") val dateEnd: String
)

