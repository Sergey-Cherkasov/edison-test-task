package pt.svcdev.trip.planning.rest.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import pt.svcdev.trip.planning.rest.databinding.ActivityWeatherPlanningBinding
import pt.svcdev.trip.planning.rest.model.weather.CurrentWeather
import pt.svcdev.trip.planning.rest.model.weather.WeatherAppState
import pt.svcdev.trip.planning.rest.repository.RepositoryImpl
import pt.svcdev.trip.planning.rest.repository.datasource.RetrofitImpl
import pt.svcdev.trip.planning.rest.viewmodel.WeatherPlanningViewModel
import java.util.ArrayList

private const val PERMISSION_REQUEST_CODE = 100

class WeatherPlanningActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWeatherPlanningBinding

    private val viewModel: WeatherPlanningViewModel by lazy {
        WeatherPlanningViewModel(RepositoryImpl(RetrofitImpl()))
    }

    private lateinit var locations: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherPlanningBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestPermissions()
        locations = intent.getStringArrayListExtra(
            "pt.svcdev.trip.planning.rest.view.PlacesRestPlanningActivity.locations"
        ) as MutableList<String>

        if (locations.isNotEmpty()) locations.forEach { location ->
            Log.d("SAMPLE_APP", location) }

        requestPermissions()

        initView()

        initViewModel()

        viewModel.getData("-55", "-86")
    }

    private fun initView() {
        binding.btnFinish.setOnClickListener {
            startActivity(Intent(this, ScreenResultActivity::class.java).apply {
                putExtra(
                    "pt.svcdev.trip.planning.rest.view.WeatherPlanningActivity.locations",
                    locations as ArrayList<String>?
                )
            })
        }
    }

    private fun requestPermissions() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            requestLocation()
        } else if (!ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.CALL_PHONE
            )
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), PERMISSION_REQUEST_CODE
            )
        }

    }

    private fun initViewModel() {
        viewModel.subscribe().observe(this@WeatherPlanningActivity, { renderData(it) })
    }

    private fun renderData(weatherAppState: WeatherAppState) {
        when (weatherAppState) {
            is WeatherAppState.Success -> {
                weatherAppState.data?.let {
                    Log.d("TEST_APP", it.basicParameters.toString())
                    setData(it)
                }
            }
            is WeatherAppState.Error -> {
            }
        }
    }

    private fun setData(data: CurrentWeather) {
        binding.weatherLocalComponent.txtNameLocation.text = data.name
        binding.weatherLocalComponent.valueLocalTemperature.text = data.basicParameters.temp.toString()
        binding.weatherLocalComponent.valueLocalPressure.text = data.basicParameters.pressure.toString()
        binding.weatherLocalComponent.valueLocalHumidity.text = data.basicParameters.humidity.toString()
        binding.weatherLocalComponent.valueWind.text = data.wind.speed.toString()
        locations.add(0, data.name)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.size == 2 && (grantResults[0] == PackageManager.PERMISSION_GRANTED ||
                        grantResults[1] == PackageManager.PERMISSION_GRANTED)
            ) {
                requestLocation()
            }
        }
    }

    private fun requestLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) return

        val locationManager: LocationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        val criteria = Criteria()
        criteria.accuracy = Criteria.ACCURACY_COARSE
        val provider = locationManager.getBestProvider(criteria, true)
        provider?.let {
            locationManager.requestLocationUpdates(it, 10000, 50F,
                object: LocationListener {
                    override fun onLocationChanged(location: Location) {
                        viewModel.getData(location.latitude.toString(), location.longitude.toString())
                    }

                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                    }
                }
            )
        }
    }

}