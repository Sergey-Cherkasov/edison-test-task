package pt.svcdev.trip.planning.rest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import pt.svcdev.trip.planning.rest.model.weather.WeatherAppState
import pt.svcdev.trip.planning.rest.model.weather.CurrentWeather
import pt.svcdev.trip.planning.rest.repository.Repository

class WeatherPlanningViewModel(private val repository: Repository<CurrentWeather>) : ViewModel() {
    private val mutableLiveData: MutableLiveData<WeatherAppState> = MutableLiveData()
    private val liveDataForViewToObserve: LiveData<WeatherAppState> = mutableLiveData

    private val viewModelCoroutineScope = CoroutineScope(
        Dispatchers.Main +
                SupervisorJob() +
                CoroutineExceptionHandler { _, throwable -> handleError(throwable) }
    )

    fun subscribe(): LiveData<WeatherAppState> = liveDataForViewToObserve

    fun getData(lat: String, lon: String) {
        mutableLiveData.value = WeatherAppState.Loading(null)
        cancelJob()
        viewModelCoroutineScope.launch {
            withContext(Dispatchers.IO) { mutableLiveData.postValue(runSearch(lat, lon)) }
        }
    }

    private suspend fun runSearch(lat: String, lon: String): WeatherAppState =
        WeatherAppState.Success(repository.getData(lat, lon))

    private fun cancelJob() {
        viewModelCoroutineScope.coroutineContext.cancelChildren()
    }

    fun handleError(error: Throwable) {
        mutableLiveData.postValue(WeatherAppState.Error(error))
    }

    override fun onCleared() {
        mutableLiveData.value = WeatherAppState.Success(null)
        super.onCleared()
    }
}