package com.example.weather.logic.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PlaceManage(var name: String, var lng: String, var lat: String,
                    var address: String, var realtimeTem: Int, var skycon: String) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
