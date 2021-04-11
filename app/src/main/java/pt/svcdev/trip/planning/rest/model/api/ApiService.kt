package pt.svcdev.trip.planning.rest.model.api

import kotlinx.coroutines.Deferred
import pt.svcdev.trip.planning.rest.model.weather.CurrentWeather
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/data/2.5/weather")
    fun getCurrentWeatherByCoord(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("units") units: String = "metric",
        @Query("appid") appId: String = API_KEY,
    ): Deferred<CurrentWeather>

    @GET("/data/2.5/weather")
    fun getCurrentWeatherByLocation(
        @Query("q") location: String,
        @Query("units") units: String = "metric",
        @Query("appid") appId: String = API_KEY,
    ): Deferred<CurrentWeather>

}

private const val API_KEY = "2774380a7fbc3859b8bbe154984844b5"