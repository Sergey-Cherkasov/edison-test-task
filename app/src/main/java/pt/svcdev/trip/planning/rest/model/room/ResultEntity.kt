package pt.svcdev.trip.planning.rest.model.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class ResultEntity(
    @field:PrimaryKey(autoGenerate = true)
    @field:ColumnInfo(name = "id")
    val id: Long,

    @field:ColumnInfo(name = "date_start")
    val dateStart: String,

    @field:ColumnInfo(name = "date_end")
    val dateEnd: String
)