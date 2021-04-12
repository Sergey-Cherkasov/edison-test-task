package pt.svcdev.trip.planning.rest.model.room

import androidx.room.Embedded
import androidx.room.Relation

class ResultModel(
    @Embedded
    val result: ResultEntity,

    @Relation(parentColumn = "id", /*entity = LocationEntity::class,*/ entityColumn = "result_id")
    val locations: List<LocationEntity>
)