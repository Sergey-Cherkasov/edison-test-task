package pt.svcdev.trip.planning.rest.repository.datasource

interface DataSource<T> {
    suspend fun getData(lat: String, lon: String): T
    suspend fun getData(location: String): T
}