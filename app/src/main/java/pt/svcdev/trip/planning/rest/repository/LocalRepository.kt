package pt.svcdev.trip.planning.rest.repository

import android.content.Context
import pt.svcdev.trip.planning.rest.model.room.ResultModel

interface LocalRepository<T> {
    suspend fun save2db(context: Context, data: T)
}