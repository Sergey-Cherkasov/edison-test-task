package pt.svcdev.trip.planning.rest.model.weather

sealed class WeatherAppState {
    data class Success(val data: CurrentWeather?) : WeatherAppState()
    data class Error(val error: Throwable) : WeatherAppState()
    data class Loading(val progress: Int?) : WeatherAppState()
}