package pt.svcdev.trip.planning.rest.view

import android.content.Intent
import android.content.Intent.ACTION_SEND
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

    private lateinit var temperature: String
    private lateinit var pressure: String
    private lateinit var humidity: String
    private lateinit var wind: String
    private lateinit var dateStart: String
    private lateinit var dateEnd: String



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
        dateStart = intent.getBundleExtra(
            "pt.svcdev.trip.planning.rest.view.WeatherPlanningActivity.bundle"
        )!!["pt.svcdev.trip.planning.rest.view.PlacesRestPlanningActivity.dateStart"].toString()
        dateEnd = intent.getBundleExtra(
            "pt.svcdev.trip.planning.rest.view.WeatherPlanningActivity.bundle"
        )!!["pt.svcdev.trip.planning.rest.view.PlacesRestPlanningActivity.dateEnd"].toString()


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
                runShare()
                return true
            }
        }
        return false
    }

    private fun runShare() {
        startActivity(Intent.createChooser(Intent(ACTION_SEND), "Choose application"))
    }

    private fun initViewModel() {
        viewModel.subscribeLiveDataWeather().observe(this@ScreenResultActivity,
            {renderData(it)})
    }

    private fun renderData(listResult: Map<String, CurrentWeather>?) {
        setDataToAdapter(listResult)
    }

    private fun initView() {
        binding.listResult.layoutManager = LinearLayoutManager(applicationContext)
        binding.listResult.adapter = adapter
        binding.btnSaveResult.setOnClickListener {
            save2db(resultModelList)
        }
    }

    private fun setDataToAdapter(listResult: Map<String, CurrentWeather>?) {
        if (listResult != null) {
            resultModelList = listResult
            adapter.setData(resultModelList)
        }
    }

    private fun save2db(listResult: Map<String, CurrentWeather>) {
        viewModel.save2Db(this , listResult, dateStart, dateEnd)
    }

}