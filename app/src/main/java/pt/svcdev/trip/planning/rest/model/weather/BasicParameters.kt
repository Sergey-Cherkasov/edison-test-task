package pt.svcdev.trip.planning.rest.model.weather

import com.google.gson.annotations.SerializedName

data class BasicParameters(
    @field:SerializedName("temp") val temp: Double,
    @field:SerializedName("pressure") val pressure: Int,
    @field:SerializedName("humidity") val humidity: Int
)
