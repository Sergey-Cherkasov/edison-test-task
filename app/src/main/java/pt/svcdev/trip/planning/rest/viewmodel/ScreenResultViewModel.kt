package pt.svcdev.trip.planning.rest.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import pt.svcdev.trip.planning.rest.model.room.ResultModel
import pt.svcdev.trip.planning.rest.model.weather.CurrentWeather
import pt.svcdev.trip.planning.rest.repository.LocalRepositoryImpl
import pt.svcdev.trip.planning.rest.repository.Repository

class ScreenResultViewModel(
    private val repository: Repository<CurrentWeather>,
    localRepository: LocalRepositoryImpl
) : ViewModel() {

    private val coroutineScope = CoroutineScope(
        Dispatchers.Main +
                SupervisorJob()
    )

    private val mutableLiveDataWeather: MutableLiveData<Map<String, CurrentWeather>> = MutableLiveData()
    private val liveDataWeatherForViewToObserve = mutableLiveDataWeather
//    private val mutableLiveDataResult: MutableLiveData<ResultModel> = MutableLiveData()
//    private val liveDataResultForViewToObserve = mutableLiveDataResult

    fun subscribeLiveDataWeather() = liveDataWeatherForViewToObserve
//    fun subscribeLiveDataResult() = liveDataResultForViewToObserve

    fun getData(locations: List<String>) {
        cancelJob()
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                val listLocationWeather: MutableMap<String, CurrentWeather> = mutableMapOf()
                locations.forEach { location ->
                    listLocationWeather.put(
                        location,
                        repository.getData(location/*.replace(" ", "%20")*/)
                    )
                }
                mutableLiveDataWeather.postValue(listLocationWeather)
            }
        }
    }

    private fun cancelJob() {
        coroutineScope.coroutineContext.cancelChildren()
    }

}