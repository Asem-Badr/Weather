package com.example.weather.repository

import com.example.weather.model.CurrentWeatherResponse
import com.example.weather.model.DisplayableWeatherData
import com.example.weather.model.ForecastResponse
import kotlinx.coroutines.flow.Flow

interface IRepository {
    suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        appId: String
    ): Flow<CurrentWeatherResponse>

    suspend fun getFiveDayForecast(
        lat: Double,
        lon: Double,
        appId: String
    ): Flow<ForecastResponse>

    fun getFavoriteLocations(): Flow<List<DisplayableWeatherData>>
    suspend fun addToFav(location: DisplayableWeatherData)

    suspend fun removeFromFav(location: DisplayableWeatherData)

    suspend fun searchForWeather(location: String): Flow<DisplayableWeatherData?>
}