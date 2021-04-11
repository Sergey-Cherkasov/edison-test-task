package pt.svcdev.trip.planning.rest.repository

import pt.svcdev.trip.planning.rest.model.weather.CurrentWeather
import pt.svcdev.trip.planning.rest.repository.datasource.DataSource

class RepositoryImpl(private val dataSource: DataSource<CurrentWeather>) :
    Repository<CurrentWeather> {

    override suspend fun getData(lat: String, lon: String): CurrentWeather =
        dataSource.getData(lat, lon)

    override suspend fun getData(location: String): CurrentWeather =
        dataSource.getData(location)
}