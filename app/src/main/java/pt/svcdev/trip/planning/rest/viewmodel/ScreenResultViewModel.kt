package pt.svcdev.trip.planning.rest.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import kotlinx.coroutines.*
import pt.svcdev.trip.planning.rest.model.room.LocationEntity
import pt.svcdev.trip.planning.rest.model.room.ResultEntity
import pt.svcdev.trip.planning.rest.model.room.ResultModel
import pt.svcdev.trip.planning.rest.model.weather.CurrentWeather
import pt.svcdev.trip.planning.rest.repository.LocalRepositoryImpl
import pt.svcdev.trip.planning.rest.repository.Repository
import pt.svcdev.trip.planning.rest.utils.map2json

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
//    private val mutableLiveDataResult: MutableLiveData<ResultModel> = MutableLiveData()
//    private val liveDataResultForViewToObserve = mutableLiveDataResult

    fun subscribeLiveDataWeather() = liveDataWeatherForViewToObserve
//    fun subscribeLiveDataResult() = liveDataResultForViewToObserve

    fun save2Db(
        context: Context,
        listResult: Map<String, CurrentWeather>,
        dateStart: String,
        dateEnd: String
    ) {
        cancelJob()
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                localRepository.save2db(context, convertToResultModel(listResult, dateStart, dateEnd))
            }
        }

    }

    private fun convertToResultModel(
        listResult: Map<String, CurrentWeather>,
        dateStart: String,
        dateEnd: String
    ): ResultModel {
        val locationList: MutableList<LocationEntity> = mutableListOf()
        listResult.entries.forEach { value ->
            locationList.add(
                LocationEntity(
                    0,
                    location = value.key,
                    temperature = value.value.basicParameters.temp.toString(),
                    0
                )
            )
        }
        val resultModel = ResultModel(
            ResultEntity(0, dateStart = dateStart, dateEnd = dateEnd),
            locationList
        )

        Log.d("TEST_APP", Gson().toJson(resultModel))

//        val result = map2json(listResult, dateStart, dateEnd)
//        Log.d("TEST_APP", result)
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
                mutableLiveDataWeather.postValue(
                    listLocationWeather.toList().sortedByDescending { (_, value) ->
                        value.basicParameters.temp
                    }.toMap()
                )
            }
        }
    }

    private fun cancelJob() {
        coroutineScope.coroutineContext.cancelChildren()
    }

}