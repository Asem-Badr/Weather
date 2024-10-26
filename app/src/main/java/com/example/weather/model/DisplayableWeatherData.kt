package com.example.weather.model

data class DisplayableWeatherData(
    val weatherDescription: String,
    val weatherIconUrl: String,
    val currentDay: String,
    val currentDate: String,
    val temperature: String,
    val locationDescription: String,
    val pressure: String,
    val windSpeed: String,
    val humidity: String,
    val cloudCoverage: String,
    val hourlyForecast: List<DisplayableHourlyForecast>,
    val dailyForecast: List<DisplayableDailyForecast>
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
