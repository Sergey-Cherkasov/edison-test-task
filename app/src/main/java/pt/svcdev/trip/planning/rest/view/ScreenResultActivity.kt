package pt.svcdev.trip.planning.rest.view

import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
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

    private val coroutineScope = CoroutineScope(
        Dispatchers.Main +
                SupervisorJob() +
                CoroutineExceptionHandler { _, throwable -> handleError(throwable)}
    )

    private val viewModel: ScreenResultViewModel by lazy {
        ScreenResultViewModel(RepositoryImpl(RetrofitImpl()), LocalRepositoryImpl(RoomDatabaseImpl()))
    }

    private val adapter: ScreenResultRVAdapter by lazy { ScreenResultRVAdapter() }

    private var resultModelList: Map<String, CurrentWeather> = mapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScreenResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val locations = intent.getStringArrayListExtra(
            "pt.svcdev.trip.planning.rest.view.WeatherPlanningActivity.locations"
        ) as MutableList<String>

        initViewModel()
        viewModel.getData(locations)
        initView()

        getResult()

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

    private fun getResult() {
        cancelJod()
        coroutineScope.launch {
            withContext(Dispatchers.IO) {

            }
        }
    }

    private fun initView() {
        binding.listResult.layoutManager = LinearLayoutManager(applicationContext)
        binding.listResult.adapter = adapter
        binding.btnSaveResult.setOnClickListener {
            save2db()
        }
    }

    private fun setDataToAdapter(listResult: Map<String, CurrentWeather>?) {
        if (listResult != null) {
            resultModelList = listResult
            adapter.setData(resultModelList)
        }
    }

    private fun save2db() {
        cancelJod()
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
            }
        }
    }

    private fun cancelJod() {
        coroutineScope.coroutineContext.cancelChildren()
    }

    private fun handleError(throwable: Throwable) {
    }

}