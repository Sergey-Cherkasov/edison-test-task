package pt.svcdev.trip.planning.rest.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import pt.svcdev.trip.planning.rest.R
import pt.svcdev.trip.planning.rest.model.weather.CurrentWeather

class ScreenResultRVAdapter() : RecyclerView.Adapter<ScreenResultRVAdapter.ViewHolder>() {

    private var locationList: List<String> = listOf()
    private var data: Map<String, CurrentWeather> = mapOf()
    private var localLocation: String = ""

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val valueLocation: TextView = view.findViewById(R.id.value_location)
        private val valueTemperature: TextView = view.findViewById(R.id.value_temperature)
        private val cardView: MaterialCardView = view.findViewById(R.id.card_view)

        fun bind(location: String, weather: CurrentWeather?) {
            valueLocation.text = location
            valueTemperature.text = weather?.basicParameters?.temp.toString()
            if (location.equals(localLocation)) cardView.elevation = 24F
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_list_result_item, parent, false) as View)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(locationList[position], data[locationList[position]])

    override fun getItemCount(): Int = data.size

    fun setData(localLocation: String, data: Map<String, CurrentWeather>) {
        this.locationList = data.keys.toList()
        this.data = data
        this.localLocation = localLocation
        notifyDataSetChanged()
    }

}