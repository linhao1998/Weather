package com.example.weather.ui.placemanage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.logic.Repository
import com.example.weather.logic.model.Location
import com.example.weather.logic.model.Place
import com.example.weather.logic.model.PlaceManage
import com.example.weather.ui.weather.WeatherActivity

class PlaceManageAdapter(private val context: WeatherActivity, private val placeManageList: List<PlaceManage>): RecyclerView.Adapter<PlaceManageAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val placeManageName: TextView = view.findViewById(R.id.placeManageName)
        val placeManageSkycon: TextView = view.findViewById(R.id.placeManageSkycon)
        val placeManageAddress: TextView = view.findViewById(R.id.placeManageAddress)
        val placeManageRealtimeTem: TextView = view.findViewById(R.id.placeManageRealtimeTem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.place_manage_item,parent,false)
        val holder = ViewHolder(view)
        holder.itemView.setOnClickListener {
            val position = holder.bindingAdapterPosition
            val placeManage = placeManageList[position]
            context.drawerLayout.closeDrawers()
            context.weatherViewModel.locationLng = placeManage.lng
            context.weatherViewModel.locationLat = placeManage.lat
            context.weatherViewModel.placeName = placeManage.name
            context.refreshWeather()
            context.weatherViewModel.isUpdatePlaceManage = 1
            val place = Place(placeManage.name, Location(placeManage.lng,placeManage.lat),placeManage.address)
            context.placeManageViewModel.savePlace(place)
        }
        return holder
    }

    override fun getItemCount(): Int {
        return placeManageList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val placeManage = placeManageList[position]
        holder.placeManageName.text = placeManage.name
        holder.placeManageAddress.text = placeManage.address
        holder.placeManageSkycon.text = placeManage.skycon
        val realtimeTemInfo = "${placeManage.realtimeTem}°"
        holder.placeManageRealtimeTem.text = realtimeTemInfo
    }
}