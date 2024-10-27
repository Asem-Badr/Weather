package com.example.weather.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.productslist.db.Dao
import com.example.weather.model.DisplayableWeatherData

@Database(entities = arrayOf(DisplayableWeatherData::class), version = 1)
@TypeConverters(Converters::class)
abstract class LocationsDatabase : RoomDatabase(){
    abstract fun getLocationsDao(): Dao
    companion object{
        @Volatile
        private var INSTANCE : LocationsDatabase? = null

        fun getInstance (ctx : Context): LocationsDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    ctx.applicationContext, LocationsDatabase::class.java, "locations"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}