package com.example.weather.logic.model

data class Weather(val realtime: RealtimeResponse.Realtime, val hourly: HourlyResponse.Hourly, val daily: DailyResponse.Daily)
