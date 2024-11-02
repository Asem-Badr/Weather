package com.example.weather.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
data class DisplayableWeatherData(
    val weatherDescription: String,
    val weatherIconUrl: String,
    val currentDay: String,
    val currentDate: String,
    val temperature: String,
    @PrimaryKey var locationDescription: String,
    val pressure: String,
    val windSpeed: String,
    val humidity: String,
    val cloudCoverage: String,
    val hourlyForecast: List<DisplayableHourlyForecast>,
    val dailyForecast: List<DisplayableDailyForecast>,
    val latitude:Double,
    val longitude:Double,
    val timeStamp : Long
)

data class DisplayableHourlyForecast(
    val time: String,
    val temp: String,
    val iconUrl: String
)

data class DisplayableDailyForecast(
    val day: String,
    val temp: String,
    val iconUrl: String
)
