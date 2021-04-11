package pt.svcdev.trip.planning.rest.model.weather

import com.google.gson.annotations.SerializedName

data class Wind(
    @field:SerializedName("speed") val speed: Double,
    @field:SerializedName("deg") val deg: Double
)