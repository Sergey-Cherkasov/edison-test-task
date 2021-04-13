package pt.svcdev.trip.planning.rest.repository

import android.content.Context
import pt.svcdev.trip.planning.rest.model.room.DatePlaningWithLocationWeather
import pt.svcdev.trip.planning.rest.model.room.ResultModel
import pt.svcdev.trip.planning.rest.model.weather.CurrentWeather
import pt.svcdev.trip.planning.rest.repository.datasource.LocalDataSource

class LocalRepositoryImpl(private val localDataSource: LocalDataSource<DatePlaningWithLocationWeather>) :
    LocalRepository<DatePlaningWithLocationWeather> {

    override suspend fun save2db(context: Context, data: DatePlaningWithLocationWeather) {
        localDataSource.save2db(context, data)
    }

}