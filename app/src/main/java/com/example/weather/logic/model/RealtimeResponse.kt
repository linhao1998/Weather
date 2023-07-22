package com.example.weather.logic.model

import com.google.gson.annotations.SerializedName

data class RealtimeResponse(val status: String, val result: Result) {

    data class Result(val realtime: Realtime)

    data class Realtime(val skycon: String, val temperature: Float, val humidity: Float, val wind: Wind,
                    @SerializedName("apparent_temperature") val apparentTemperature: Float,
                    @SerializedName("air_quality") val airQuality: AirQuality)

    data class AirQuality(val aqi: AQI)

    data class AQI(val chn: Float)

    data class Wind(val speed: Float, val direction: Float)

}
