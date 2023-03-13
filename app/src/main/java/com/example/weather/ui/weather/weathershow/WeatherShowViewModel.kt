package com.example.weather.ui.weather.weathershow

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.weather.logic.Repository
import com.example.weather.logic.model.Location

class WeatherShowViewModel: ViewModel() {

    private val locationLiveData = MutableLiveData<Location>()

    var locationLng = ""

    var locationLat = ""

    var placeName = ""

    var placeAddress = ""

    var placeRealtimeTem = -100

    var placeSkycon = ""

    var isUpdatePlaceManage = 0

    val weatherLiveData = Transformations.switchMap(locationLiveData) { location ->
        Repository.refreshWeather(location.lng,location.lat)
    }

    fun refreshWeather(lng: String, lat: String) {
        locationLiveData.value = Location(lng, lat)
    }

}