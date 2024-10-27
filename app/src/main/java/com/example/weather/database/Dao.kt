package com.example.productslist.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weather.model.DisplayableWeatherData
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Query("Select * from locations")
    fun getFavLocations() : Flow<List<DisplayableWeatherData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(location : DisplayableWeatherData): Long

    @Delete
    suspend fun delete(location : DisplayableWeatherData): Int
}