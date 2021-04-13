package pt.svcdev.trip.planning.rest.repository.datasource

import android.content.Context
import androidx.room.Room
import pt.svcdev.trip.planning.rest.model.room.ResultDao
import pt.svcdev.trip.planning.rest.model.room.ResultDatabase
import pt.svcdev.trip.planning.rest.model.room.ResultModel

class RoomDatabaseImpl() : LocalDataSource<ResultModel> {
    override suspend fun save2db(context: Context, data: ResultModel) {
        getInstanceDB(context).resultDao().insert(data)
    }

    private fun getInstanceDB(context: Context) =
        Room.databaseBuilder(context, ResultDatabase::class.java, "ResultDb").build()

}