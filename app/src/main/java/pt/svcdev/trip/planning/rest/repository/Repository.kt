package pt.svcdev.trip.planning.rest.repository

interface Repository<T> {
    suspend fun getData(lat: String, lon: String): T
    suspend fun getData(location: String): T
}