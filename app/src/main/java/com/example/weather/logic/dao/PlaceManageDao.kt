package com.example.weather.logic.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.weather.logic.model.PlaceManage

@Dao
interface PlaceManageDao {

    @Insert
    fun insertPlaceManage(placeManage: PlaceManage): Long

    @Update
    fun updatePlaceManage(placeManage: PlaceManage)

    @Query("select * from PlaceManage")
    fun loadAllPlaceManages():List<PlaceManage>

    @Query("select * from PlaceManage where lng = :lng and lat = :lat")
    fun querySpecifyPlaceManage(lng: String, lat: String): PlaceManage?

    @Query("delete from PlaceManage where lng = :lng and lat = :lat")
    fun deletePlaceManageByLngLat(lng: String, lat: String): Int
}