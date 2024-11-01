package com.example.weather.repository

import com.example.productslist.db.Dao
import com.example.weather.SettingsManager
import com.example.weather.model.CurrentWeatherResponse
import com.example.weather.model.DisplayableWeatherData
import com.example.weather.model.ForecastResponse
import com.example.weather.network.WeatherApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class Repository(
    val remote: WeatherApiService.RetrofitHelper,
    val local: Dao,
    val settingsManager: SettingsManager
) {
    suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        appId: String
    ): Flow<CurrentWeatherResponse> = flow {
        val serviceObject = remote.weatherService
        val result = serviceObject.getCurrentWeather(
            lat,
            lon,
            settingsManager.getLanguage(),
            settingsManager.getUnit(),
            appId
        )
        emit(result)
    }

    suspend fun getFiveDayForecast(
        lat: Double,
        lon: Double,
        appId: String
    ): Flow<ForecastResponse> = flow {
        val serviceObject = remote.weatherService
        val result = serviceObject.getFiveDayForecast(
            lat,
            lon,
            settingsManager.getLanguage(),
            settingsManager.getUnit(),
            appId
        )
        emit(result)
    }

    fun getFavoriteLocations():Flow<List<DisplayableWeatherData>>{
        return local.getFavLocations()
    }
    suspend fun addToFav(location: DisplayableWeatherData) {
        local.insert(location)
    }

    suspend fun removeFromFav(location: DisplayableWeatherData) {
        local.delete(location)
    }

    suspend fun searchForWeather(location: String): Flow<DisplayableWeatherData?> {
        return local.getLocationByDescription(location)
    }
}