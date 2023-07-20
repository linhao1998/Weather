package com.example.weather.ui.weather.placemanage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.weather.logic.Repository
import com.example.weather.logic.model.Location
import com.example.weather.logic.model.Place
import com.example.weather.logic.model.PlaceManage

class PlaceManageViewModel: ViewModel() {

    private val placeManageLiveData = MutableLiveData<PlaceManage>()

    private val locationLiveData = MutableLiveData<Location>()

    private val refreshLiveData = MutableLiveData<Any?>()

    val placeManageList = ArrayList<PlaceManage>()

    val addPlaceManageLiveData = Transformations.switchMap(placeManageLiveData) { placeManage ->
        Repository.addPlaceManage(placeManage)
    }

    val deletePlaceManageLiveData = Transformations.switchMap(locationLiveData) { location ->
        Repository.deletePlaceManage(location.lng,location.lat)
    }

    val loadAllPlaceManages = Transformations.switchMap(refreshLiveData) {
        Repository.loadAllPlaceManages()
    }


    fun addPlaceManage(placeManage: PlaceManage) {
        placeManageLiveData.value = placeManage
    }

    fun deletePlaceManage(lng: String, lat: String){
        locationLiveData.value = Location(lng,lat)
    }

    fun refreshPlaceManage() {
        refreshLiveData.value = refreshLiveData.value
    }

    fun savePlace(place: Place) = Repository.savePlace(place)
}