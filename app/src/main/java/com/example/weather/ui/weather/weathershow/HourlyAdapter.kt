package com.example.weather.ui.weather.weathershow

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.logic.model.HourlyForecast
import com.example.weather.logic.model.getSky
import java.text.SimpleDateFormat
import java.util.*

class HourlyAdapter(private val hourlyForecastList: List<HourlyForecast>): RecyclerView.Adapter<HourlyAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val hourlyTemperatureInfo: TextView = view.findViewById(R.id.hourlyTemperatureInfo)
        val hourlySkyIcon: ImageView = view.findViewById(R.id.hourlySkyIcon)
        val hourlyDateInfo: TextView = view.findViewById(R.id.hourlyDateInfo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.forecast_hourly_item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return hourlyForecastList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hourlyForecast = hourlyForecastList[position]
        val hourlyTemperatureInfoText = hourlyForecast.temVal.toInt()
        holder.hourlyTemperatureInfo.text = "${hourlyTemperatureInfoText}°"
        val sky = getSky(hourlyForecast.skyVal)
        holder.hourlySkyIcon.setImageResource(sky.icon)
        val simpleDateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        holder.hourlyDateInfo.text = simpleDateFormat.format(hourlyForecast.datetime)
    }
}