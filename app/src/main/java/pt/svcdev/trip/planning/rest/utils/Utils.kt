package pt.svcdev.trip.planning.rest.utils

import com.google.gson.Gson
import pt.svcdev.trip.planning.rest.model.room.LocationEntity
import pt.svcdev.trip.planning.rest.model.weather.CurrentWeather

fun map2json(listResult: Map<String, CurrentWeather>, dateStart: String, dateEnd: String): String =
    Gson().toJson(listResult)

fun json2LocationEntity(gson: String): LocationEntity =
    Gson().fromJson(gson, LocationEntity::class.java)

