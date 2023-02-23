package com.example.weather.logic

import androidx.lifecycle.liveData
import com.example.weather.logic.model.Place
import com.example.weather.logic.network.WeatherNetwork
import kotlinx.coroutines.Dispatchers
import okhttp3.Dispatcher

object Repository {

    fun searchPlaces(query: String) = liveData(Dispatchers.IO) {
        val result = try {
            val placeResponse = WeatherNetwork.searchPlaces(query)
            if (placeResponse.status == "ok") {
                val places = placeResponse.places
                Result.success(places)
            } else {
                Result.failure(java.lang.RuntimeException("response status is ${placeResponse.status}"))
            }
        } catch (e: Exception) {
            Result.failure<List<Place>>(e)
        }
        emit(result)
    }
}