package pt.svcdev.trip.planning.rest.model.room

import androidx.room.Embedded
import androidx.room.Relation

data class ResultModel(
    @Embedded
    val result: ResultEntity,

    @Relation(
        parentColumn = "id_result",
        entityColumn = "result_creator_id"
    )
    val locations: List<LocationEntity>
)