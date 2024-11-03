package com.example.weather

import android.content.Context
import com.example.productslist.db.Dao
import com.example.weather.database.LocationsDatabase
import com.example.weather.model.DisplayableWeatherData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class LocalDataSource (val dao: Dao){
    fun getFavoriteLocations(): Flow<List<DisplayableWeatherData>> {
        return dao.getFavLocations()
    }

    fun getLocationByDescription(locationDescription: String): Flow<DisplayableWeatherData?> {
        return dao.getLocationByDescription(locationDescription)
    }

    suspend fun insertLocation(location: DisplayableWeatherData): Long {
        return withContext(Dispatchers.IO) {
            dao.insert(location)
        }
    }

    suspend fun deleteLocation(location: DisplayableWeatherData): Int {
        return withContext(Dispatchers.IO) {
            dao.delete(location)
        }
    }
}