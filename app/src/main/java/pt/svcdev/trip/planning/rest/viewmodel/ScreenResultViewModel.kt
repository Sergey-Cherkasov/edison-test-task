package pt.svcdev.trip.planning.rest.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import kotlinx.coroutines.*
import pt.svcdev.trip.planning.rest.model.room.*
import pt.svcdev.trip.planning.rest.model.weather.CurrentWeather
import pt.svcdev.trip.planning.rest.repository.LocalRepositoryImpl
import pt.svcdev.trip.planning.rest.repository.Repository

class ScreenResultViewModel(
    private val repository: Repository<CurrentWeather>,
    private val localRepository: LocalRepositoryImpl
) : ViewModel() {

    private val coroutineScope = CoroutineScope(
        Dispatchers.Main +
                SupervisorJob()
    )

    private val mutableLiveDataWeather: MutableLiveData<Map<String, CurrentWeather>> =
        MutableLiveData()
    private val liveDataWeatherForViewToObserve = mutableLiveDataWeather

    fun subscribeLiveDataWeather() = liveDataWeatherForViewToObserve

    fun save2Db(
        context: Context,
        listResult: Map<String, CurrentWeather>,
        dateStart: String,
        dateEnd: String
    ) {
        cancelJob()
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                if(dateStart.isNotEmpty() && dateEnd.isNotEmpty()) {
                    localRepository.save2db(
                        context,
                        convertToModel(listResult, dateStart, dateEnd)
                    )
                }
            }
        }

    }
    private fun convertToModel(
        listResult: Map<String, CurrentWeather>,
        dateStart: String,
        dateEnd: String
    ): DatePlaningWithLocationWeather {
        val locationWeatherList: MutableList<LocationWeatherEntity> = mutableListOf()
        val datePlanningEntity = DatePlanningEntity(
            0,
            dateStart = dateStart,
            dateEnd = dateEnd
        )
        listResult.entries.forEach { value ->
            locationWeatherList.add(
                LocationWeatherEntity(
                    0,
                    locationName = value.key,
                    temp = value.value.basicParameters.temp,
                    pressure = value.value.basicParameters.pressure,
                    humidity = value.value.basicParameters.humidity,
                    wind = value.value.wind.speed,
                    datePlanningEntity.dateId
                )
            )
        }
        val resultModel = DatePlaningWithLocationWeather(
            datePlanningEntity,
            locationWeatherList
        )

        Log.d("TEST_APP", Gson().toJson(resultModel))

        return resultModel
    }

    fun getData(
        locations: List<String>,
        temperature: String,
        pressure: String,
        humidity: String,
        wind: String
    ) {
        cancelJob()
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                val listLocationWeather: MutableMap<String, CurrentWeather> = mutableMapOf()
                if (temperature.isNotEmpty() && pressure.isNotEmpty() && humidity.isNotEmpty() &&
                    wind.isNotEmpty()) {
                    locations.forEach { location ->
                        val result = repository.getData(location)
                        // Filter result
                        if (result.basicParameters.temp <= temperature.toDouble() &&
                            result.basicParameters.pressure <= pressure.toInt() &&
                            result.basicParameters.humidity <= humidity.toInt() &&
                            result.wind.speed <= wind.toDouble()
                        )
                            listLocationWeather[location] = result
                    }
                    mutableLiveDataPost(listLocationWeather)
                } else {
                    locations.forEach { location ->
                        val result = repository.getData(location)
                        listLocationWeather[location] = result
                    }
                    mutableLiveDataPost(listLocationWeather)
                }
            }
        }
    }

    private fun mutableLiveDataPost(listLocationWeather: MutableMap<String, CurrentWeather>) {
        mutableLiveDataWeather.postValue(
            listLocationWeather.toList().sortedByDescending { (_, value) ->
                value.basicParameters.temp
            }.toMap()
        )
    }

    private fun cancelJob() {
        coroutineScope.coroutineContext.cancelChildren()
    }

}