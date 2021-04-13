package pt.svcdev.trip.planning.rest.repository.datasource

import android.content.Context
import androidx.room.Room
import pt.svcdev.trip.planning.rest.model.room.*

class RoomDatabaseImpl() : LocalDataSource<DatePlaningWithLocationWeather> {
//    override suspend fun save2db(context: Context, data: ResultModel) {
//        getInstanceDB(context).resultDao().insert(data)
//    }

    override suspend fun save2db(context: Context, data: DatePlaningWithLocationWeather) {
        getInstanceDB(context).dao().insert(data)
    }

//    private fun getInstanceDB(context: Context) =
//        Room.databaseBuilder(context, ResultDatabase::class.java, "ResultDb").build()

    private fun getInstanceDB(context: Context) =
        Room.databaseBuilder(context, RestPlanningDatabase::class.java, "RestPlanningDB")
            .build()

}