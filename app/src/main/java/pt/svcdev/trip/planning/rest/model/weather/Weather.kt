package pt.svcdev.trip.planning.rest.model.weather

import com.google.gson.annotations.SerializedName

data class Weather(
    @field:SerializedName("description") val description: String
)