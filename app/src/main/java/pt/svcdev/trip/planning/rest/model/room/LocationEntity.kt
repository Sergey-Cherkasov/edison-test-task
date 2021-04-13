package pt.svcdev.trip.planning.rest.model.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = ResultEntity::class,
            parentColumns = ["result_id"],
            childColumns = ["result_creator_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.RESTRICT,
            deferred = true
        )
    ]
)
data class LocationEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long,

    @ColumnInfo(name = "location") val location: String,

    @ColumnInfo(name = "temperature") val temperature: String,

    @ColumnInfo(name = "result_creator_id") val resultId: Long
)