package pt.svcdev.trip.planning.rest.model.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = ResultEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("result_id"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
class LocationEntity(
    @field:PrimaryKey
    @field:ColumnInfo(name = "id")
    val id: Long,

    @field:ColumnInfo(name = "location")
    val location: String,

    @field:ColumnInfo(name = "temperature")
    val temperature: String,

    @field:ColumnInfo(name = "result_id")
    val resultId: Long
)