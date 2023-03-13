package com.example.weather.ui.placesearch

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.logic.model.Place
import com.example.weather.ui.weather.WeatherActivity

class PlaceSearchAdapter(private val placeSearchActivity: PlaceSearchActivity, private val placeList: List<Place>): RecyclerView.Adapter<PlaceSearchAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val placeName: TextView = view.findViewById(R.id.placeName)
        val placeAddress: TextView = view.findViewById(R.id.placeAddress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.place_search_item,parent,false)
        val holder = ViewHolder(view)
        holder.itemView.setOnClickListener {
            val position = holder.bindingAdapterPosition
            val place = placeList[position]
            val intent = Intent(placeSearchActivity,WeatherActivity::class.java).apply {
                putExtra("location_lng", place.location.lng)
                putExtra("location_lat", place.location.lat)
                putExtra("place_name", place.name)
                putExtra("place_address",place.address)
            }
            placeSearchActivity.viewModel.savePlace(place)
            placeSearchActivity.startActivity(intent)
            placeSearchActivity.finish()
        }
        return holder
    }

    override fun getItemCount(): Int {
        return placeList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = placeList[position]
        holder.placeName.text = place.name
        holder.placeAddress.text = place.address
    }
}