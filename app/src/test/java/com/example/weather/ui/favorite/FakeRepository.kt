package com.example.weather.ui.favorite

import com.example.weather.model.CurrentWeatherResponse
import com.example.weather.model.DisplayableWeatherData
import com.example.weather.model.ForecastResponse
import com.example.weather.repository.IRepository
import kotlinx.coroutines.flow.Flow

class FakeRepository:IRepository {
    override suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        appId: String
    ): Flow<CurrentWeatherResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getFiveDayForecast(
        lat: Double,
        lon: Double,
        appId: String
    ): Flow<ForecastResponse> {
        TODO("Not yet implemented")
    }

    override fun getFavoriteLocations(): Flow<List<DisplayableWeatherData>> {
        TODO("Not yet implemented")
    }

    override suspend fun addToFav(location: DisplayableWeatherData) {
        TODO("Not yet implemented")
    }

    override suspend fun removeFromFav(location: DisplayableWeatherData) {
        TODO("Not yet implemented")
    }

    override suspend fun searchForWeather(location: String): Flow<DisplayableWeatherData?> {
        TODO("Not yet implemented")
    }
}