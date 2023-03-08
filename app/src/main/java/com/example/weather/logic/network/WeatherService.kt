package com.example.weather.logic.network

import com.example.weather.WeatherApplication
import com.example.weather.logic.model.DailyResponse
import com.example.weather.logic.model.RealtimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherService {

    @GET("v2.6/${WeatherApplication.TOKEN}/{lng},{lat}/realtime")
    fun getRealtimeWeather(@Path("lng") lng: String, @Path("lat") lat: String): Call<RealtimeResponse>

    @GET("v2.6/${WeatherApplication.TOKEN}/{lng},{lat}/daily?dailysteps=7")
    fun getDailyWeather(@Path("lng") lng: String, @Path("lat") lat: String): Call<DailyResponse>
}