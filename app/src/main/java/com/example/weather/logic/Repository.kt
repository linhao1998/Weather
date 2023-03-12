package com.example.weather.logic

import androidx.lifecycle.liveData
import com.example.weather.WeatherApplication
import com.example.weather.logic.dao.PlaceSearchDao
import com.example.weather.logic.model.Place
import com.example.weather.logic.model.PlaceManage
import com.example.weather.logic.model.Weather
import com.example.weather.logic.network.WeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.CoroutineContext

object Repository {

    private val placeManageDao = AppDatabase.getDatabase(WeatherApplication.context).placeManageDao()

    fun searchPlaces(query: String) = fire(Dispatchers.IO) {
        val placeResponse = WeatherNetwork.searchPlaces(query)
        if (placeResponse.status == "ok") {
            val places = placeResponse.places
            Result.success(places)
        } else {
            Result.failure(java.lang.RuntimeException("response status is ${placeResponse.status}"))
        }
    }

    fun refreshWeather(lng: String, lat: String) = fire(Dispatchers.IO) {
        coroutineScope {
            val deferredRealtime = async {WeatherNetwork.getRealtimeWeather(lng,lat)}
            val deferredHourly = async { WeatherNetwork.getHourlyWeather(lng,lat) }
            val deferredDaily = async {WeatherNetwork.getDailyWeather(lng,lat)}
            val realtimeResponse = deferredRealtime.await()
            val hourlyResponse = deferredHourly.await()
            val dailyResponse = deferredDaily.await()
            if (realtimeResponse.status == "ok" && hourlyResponse.status == "ok" && dailyResponse.status == "ok") {
                val weather = Weather(realtimeResponse.result.realtime, hourlyResponse.result.hourly, dailyResponse.result.daily)
                Result.success(weather)
            } else {
                Result.failure(java.lang.RuntimeException(
                    "realtime response status is ${realtimeResponse.status}" +
                    "hourly response status is ${hourlyResponse.status}" +
                    "daily response status is ${dailyResponse.status}"
                ))
            }
        }
    }

    fun savePlace(place: Place) = PlaceSearchDao.savePlace(place)

    fun getSavedPlace() = PlaceSearchDao.getSavedPlace()

    fun isPlaceSaved() = PlaceSearchDao.isPlaceSaved()

    fun addPlaceManage(placeManage: PlaceManage) = fire(Dispatchers.IO) {
        coroutineScope {
            val queryPlaceManage = async { placeManageDao.querySpecifyPlaceManage(placeManage.lng,placeManage.lat) }.await()
            if (queryPlaceManage == null) {
                async { placeManageDao.insertPlaceManage(placeManage) }.await()
            } else {
                queryPlaceManage.realtimeTem = placeManage.realtimeTem
                queryPlaceManage.skycon = placeManage.skycon
                async { placeManageDao.updatePlaceManage(queryPlaceManage)}.await()
            }
            val placeManageList = async { placeManageDao.loadAllPlaceManages() }.await()
            Result.success(placeManageList)
        }
    }

    fun deletePlaceManage(lng: String, lat: String) = fire(Dispatchers.IO) {
        coroutineScope {
            async { placeManageDao.deletePlaceManageByLngLat(lng, lat) }.await()
            val placeManageList = async { placeManageDao.loadAllPlaceManages() }.await()
            Result.success(placeManageList)
        }
    }

    fun loadAllPlaceManages() = fire(Dispatchers.IO) {
        coroutineScope {
            val placeManageList = async { placeManageDao.loadAllPlaceManages() }.await()
            Result.success(placeManageList)
        }
    }

    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData(context) {
            val result = try {
                block()
            } catch (e: Exception) {
                Result.failure(e)
            }
            emit(result)
        }
}