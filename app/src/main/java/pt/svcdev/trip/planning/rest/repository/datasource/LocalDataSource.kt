package pt.svcdev.trip.planning.rest.repository.datasource

import android.content.Context
import pt.svcdev.trip.planning.rest.model.room.ResultModel

interface LocalDataSource<T> {

    suspend fun save2db(context: Context, data: T)

}