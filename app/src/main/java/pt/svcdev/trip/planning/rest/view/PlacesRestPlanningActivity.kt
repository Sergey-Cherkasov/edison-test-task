package pt.svcdev.trip.planning.rest.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.Pair
import androidx.core.view.children
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import pt.svcdev.trip.planning.rest.R
import pt.svcdev.trip.planning.rest.databinding.ActivityPlacesRestPlanningBinding
import java.util.*

class PlacesRestPlanningActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlacesRestPlanningBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlacesRestPlanningBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()

    }

    private fun initView() {
        // Инициализируем и показываем календарь для выбора диапазона дат
        binding.datePlanning.dateRange.setOnClickListener {
            val dateRangePicker =
                MaterialDatePicker.Builder.dateRangePicker()
                    .setTitleText("Select dates")
                    .setSelection(
                        Pair(
                            MaterialDatePicker.todayInUtcMilliseconds(),
                            MaterialDatePicker.todayInUtcMilliseconds().plus(86400000 * 3)
                        )
                    ).build()

            dateRangePicker.addOnPositiveButtonClickListener {
                binding.datePlanning.dateStart.setText(dateRangePicker.selection?.first?.let { it1 ->
                    Date(it1)
                }.toString())
                binding.datePlanning.dateEnd.setText(dateRangePicker.selection?.second?.let { it1 ->
                    Date(it1)
                }.toString())
            }

            dateRangePicker.show(supportFragmentManager, "calender")
        }

        // Настраиваем clickListener для добавления нового поля для ввода города
        binding.locationPlanning.addLocation.setOnClickListener {
            var counter = 0
            val view: View = layoutInflater.inflate(R.layout.location_item, null)
            view.findViewById<TextInputLayout>(R.id.til_location).setEndIconOnClickListener {
                binding.locationPlanning.listLocation.removeView(view)
            }
            view.findViewById<TextInputEditText>(R.id.txt_location).tag = counter++.toString()
            binding.locationPlanning.listLocation.addView(view)
        }

        // Настраиваем clickListener кнопки для перехода на следующий экран
        binding.btnNextStep.setOnClickListener {
            startActivity(Intent(this, WeatherPlanningActivity::class.java).apply {
                val locationList: MutableList<String> = mutableListOf()
                binding.locationPlanning.listLocation.children.forEach { child ->
                    locationList.add(
                        child.findViewById<TextInputEditText>(R.id.txt_location).text.toString()
                    )
                }
                putExtra(
                    "pt.svcdev.trip.planning.rest.view.PlacesRestPlanningActivity.locations",
                    locationList as ArrayList<String>?
                )
            })
        }
    }
}