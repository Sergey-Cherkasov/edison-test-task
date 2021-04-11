package pt.svcdev.trip.planning.rest.repository

import android.content.Context
import pt.svcdev.trip.planning.rest.model.room.ResultModel
import pt.svcdev.trip.planning.rest.model.weather.CurrentWeather
import pt.svcdev.trip.planning.rest.repository.datasource.LocalDataSource

class LocalRepositoryImpl(private val localDataSource: LocalDataSource<ResultModel>) :
    LocalRepository<ResultModel> {

    override fun save2db(context: Context, data: ResultModel) {
        localDataSource.save2db(context, data)
    }

}