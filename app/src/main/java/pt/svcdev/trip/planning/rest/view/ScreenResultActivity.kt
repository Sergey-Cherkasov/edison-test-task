package pt.svcdev.trip.planning.rest.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.*
import pt.svcdev.trip.planning.rest.R
import pt.svcdev.trip.planning.rest.databinding.ActivityScreenResultBinding
import pt.svcdev.trip.planning.rest.model.weather.CurrentWeather
import pt.svcdev.trip.planning.rest.repository.LocalRepositoryImpl
import pt.svcdev.trip.planning.rest.repository.RepositoryImpl
import pt.svcdev.trip.planning.rest.repository.datasource.RetrofitImpl
import pt.svcdev.trip.planning.rest.repository.datasource.RoomDatabaseImpl
import pt.svcdev.trip.planning.rest.view.adapter.ScreenResultRVAdapter
import pt.svcdev.trip.planning.rest.viewmodel.ScreenResultViewModel

class ScreenResultActivity: AppCompatActivity() {

    private lateinit var binding: ActivityScreenResultBinding

//    private val coroutineScope = CoroutineScope(
//        Dispatchers.Main +
//                SupervisorJob() +
//                CoroutineExceptionHandler { _, throwable -> handleError(throwable)}
//    )

    private val viewModel: ScreenResultViewModel by lazy {
        ScreenResultViewModel(RepositoryImpl(RetrofitImpl()), LocalRepositoryImpl(RoomDatabaseImpl()))
    }

    private val adapter: ScreenResultRVAdapter by lazy { ScreenResultRVAdapter() }

    private var resultModelList: Map<String, CurrentWeather> = mapOf()

    private var temperature: String = ""
    private var pressure: String = ""
    private var humidity: String = ""
    private var wind: String = ""
    private var dateStart: String = ""
    private var dateEnd: String = ""
    private lateinit var localLocation: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScreenResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.getBundleExtra("pt.svcdev.trip.planning.rest.view.WeatherPlanningActivity.bundle"
        )?.get("pt.svcdev.trip.planning.rest.view.WeatherPlanningActivity.temperature")?.let {
            temperature = it.toString()
        }
        intent.getBundleExtra("pt.svcdev.trip.planning.rest.view.WeatherPlanningActivity.bundle"
        )?.get("pt.svcdev.trip.planning.rest.view.WeatherPlanningActivity.pressure")?.let{
            pressure = it.toString()
        }
        intent.getBundleExtra(
            "pt.svcdev.trip.planning.rest.view.WeatherPlanningActivity.bundle"
        )?.get("pt.svcdev.trip.planning.rest.view.WeatherPlanningActivity.humidity")?.let {
            humidity = it.toString()
        }
        intent.getBundleExtra(
            "pt.svcdev.trip.planning.rest.view.WeatherPlanningActivity.bundle"
        )?.get("pt.svcdev.trip.planning.rest.view.WeatherPlanningActivity.wind")?.let {
            wind = it.toString()
        }
        intent.getBundleExtra(
            "pt.svcdev.trip.planning.rest.view.WeatherPlanningActivity.bundle"
        )?.get("pt.svcdev.trip.planning.rest.view.PlacesRestPlanningActivity.dateStart")?.let {
            dateStart = it.toString()
        }
        intent.getBundleExtra(
            "pt.svcdev.trip.planning.rest.view.WeatherPlanningActivity.bundle"
        )?.get("pt.svcdev.trip.planning.rest.view.PlacesRestPlanningActivity.dateEnd")?.let {
            dateEnd = it.toString()
        }
        localLocation = intent.getBundleExtra(
            "pt.svcdev.trip.planning.rest.view.WeatherPlanningActivity.bundle"
        )!!["pt.svcdev.trip.planning.rest.view.PlacesRestPlanningActivity.localLocation"].toString()

        val locations = intent.getBundleExtra(
            "pt.svcdev.trip.planning.rest.view.WeatherPlanningActivity.bundle"
        )?.get("pt.svcdev.trip.planning.rest.view.PlacesRestPlanningActivity.locations")
                as MutableList<String>

        initViewModel()
        viewModel.getData(locations, temperature, pressure, humidity, wind)
        initView()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.screen_result_activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.share -> {
                runShare(resultModelList)
                return true
            }
        }
        return false
    }

    private fun runShare(listResult: Map<String, CurrentWeather>?) {

        val listLocationForRest = mutableListOf<String>()

        listResult?.forEach { value ->
            listLocationForRest.add(value.key)
        }

        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, listLocationForRest.toString())
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(intent, "Choose application")
        startActivity(shareIntent)
    }

    private fun initViewModel() {
        viewModel.subscribeLiveDataWeather().observe(this@ScreenResultActivity,
            {renderData(it)})
    }

    private fun renderData(listResult: Map<String, CurrentWeather>?) {
        listResult?.let { resultModelList = it }
        setDataToAdapter(resultModelList)
    }

    private fun initView() {
        binding.listResult.layoutManager = LinearLayoutManager(applicationContext)
        binding.listResult.adapter = adapter
        binding.btnSaveResult.setOnClickListener {
            save2db(resultModelList)
        }
    }

    private fun setDataToAdapter(listResult: Map<String, CurrentWeather>?) {
        adapter.setData(localLocation, resultModelList)
    }

    private fun save2db(listResult: Map<String, CurrentWeather>) {
        viewModel.save2Db(this , listResult, dateStart, dateEnd)
    }

}