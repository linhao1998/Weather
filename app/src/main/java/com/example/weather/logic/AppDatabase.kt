package com.example.weather.logic

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weather.logic.dao.PlaceManageDao
import com.example.weather.logic.model.PlaceManage

@Database(version = 1, entities = [PlaceManage::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun placeManageDao(): PlaceManageDao

    companion object {

        private var instance: AppDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): AppDatabase {
            instance?.let {
                return it
            }
            return Room.databaseBuilder(context.applicationContext,
                AppDatabase::class.java, "app_database")
                .build().apply {
                    instance = this
                }
        }
    }
}