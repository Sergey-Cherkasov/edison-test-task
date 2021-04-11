package pt.svcdev.trip.planning.rest.model.weather

import com.google.gson.annotations.SerializedName

data class CurrentWeather(
    @field:SerializedName("weather") val weather: List<Weather>,
    @field:SerializedName("main") val basicParameters: BasicParameters,
    @field:SerializedName("wind") val wind: Wind,
    @field:SerializedName("name") val name: String
)