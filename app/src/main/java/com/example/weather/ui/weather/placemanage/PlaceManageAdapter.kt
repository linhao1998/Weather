package com.example.weather.ui.weather.placemanage

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.logic.model.Location
import com.example.weather.logic.model.Place
import com.example.weather.logic.model.PlaceManage
import com.example.weather.ui.weather.WeatherActivity

class PlaceManageAdapter(private val weatherActivity: WeatherActivity, private val placeManageList: List<PlaceManage>): RecyclerView.Adapter<PlaceManageAdapter.ViewHolder>() {

    private val scaleXDown = PropertyValuesHolder.ofFloat(View.SCALE_X, 0.9f)
    private val scaleYDown = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0.9f)
    private val scaleXUp = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.0f)
    private val scaleYUp = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.0f)

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
            weatherActivity.drawerLayout.closeDrawers()
            weatherActivity.weatherViewModel.locationLng = placeManage.lng
            weatherActivity.weatherViewModel.locationLat = placeManage.lat
            weatherActivity.weatherViewModel.placeName = placeManage.name
            weatherActivity.weatherViewModel.isUpdatePlaceManage = true
            weatherActivity.refreshWeather()
            val place = Place(placeManage.name, Location(placeManage.lng,placeManage.lat),placeManage.address)
            weatherActivity.placeManageViewModel.savePlace(place)
        }
        holder.itemView.setOnLongClickListener {
            val position = holder.bindingAdapterPosition
            val placeManage = placeManageList[position]
            // 动画
            val animator = ObjectAnimator.ofPropertyValuesHolder(it, scaleXDown,scaleYDown)
            animator.duration = 200
            animator.start()
            AlertDialog.Builder(parent.context).apply {
                setTitle("删除地点")
                setMessage("是否删除地点：${placeManage.name}?")
                setCancelable(false)
                setPositiveButton("是") { dialog, _ ->
                    weatherActivity.placeManageViewModel.deletePlaceManage(placeManage.lng,placeManage.lat)
                    dialog.dismiss()
                    it.scaleX = 1.0f
                    it.scaleY = 1.0f
                }
                setNegativeButton("否") { dialog, _ ->
                    dialog.dismiss()
                    //动画
                    val deleteAnimator = ObjectAnimator.ofPropertyValuesHolder(it, scaleXUp,scaleYUp)
                    deleteAnimator.duration = 200
                    deleteAnimator.start()
                }
                show()
            }
            true
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