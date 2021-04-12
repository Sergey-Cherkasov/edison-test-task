package pt.svcdev.trip.planning.rest.utils

import com.google.gson.Gson
import pt.svcdev.trip.planning.rest.model.room.LocationEntity
import pt.svcdev.trip.planning.rest.model.room.ResultModel
import pt.svcdev.trip.planning.rest.model.weather.CurrentWeather

fun map2json(listResult: Map<String, CurrentWeather>, dateStart: String, dateEnd: String): String =
    Gson().toJson(listResult)

fun json2LocationEntity(gson: String): LocationEntity =
    Gson().fromJson(gson, LocationEntity::class.java)

/*
{
    "data_start":"21-04-2021",
    "data_end":"30-04-2021",
    "Hong Kong":{
        "main":{"humidity":65,"pressure":1015,"temp":24.33},"name":"Hong Kong","weather":[{"description":"scattered clouds"}],"wind":{"deg":270.0,"speed":4.12}
    },
    "Mountain View":{
        "main":{"humidity":71,"pressure":1012,"temp":10.17},"name":"Mountain View","weather":[{"description":"few clouds"}],"wind":{"deg":340.0,"speed":4.12}
    }
}*/
