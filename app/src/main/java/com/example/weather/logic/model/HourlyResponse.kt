package com.example.weather.logic.model

import java.util.Date

data class HourlyResponse(val status: String, val result: Result) {

    data class Result(val hourly: Hourly)

    data class Hourly(val temperature: List<Temperature>, val skycon: List<Skycon>)

    data class Temperature(val value: Float)

    data class Skycon(val value: String, val datetime: Date)
}

data class HourlyForecast(val temVal: Float, val skyVal: String, val datetime: Date)
