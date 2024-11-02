
package com.example.weather.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.productslist.db.Dao
import com.example.weather.model.DisplayableWeatherData

/**
 * The Room Database that contains the Task table.
 *
 * Note that exportSchema should be true in production databases.
 */
@Database(entities = arrayOf(DisplayableWeatherData::class), version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ToDoDatabase : RoomDatabase() {

    abstract fun WeatherDao(): Dao
}
