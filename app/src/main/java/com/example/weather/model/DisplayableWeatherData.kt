package com.example.weather.model

class DisplayableWeatherData(
    var weatherDescription: String,
    var day: String,
    var date: String,
    var currentWeatherIcon: Int,
    var temp: String,
    var location: String,
    var hourlyData: List<HourlyData>,
    var pressure: String,
    var windSpeed: String,
    var humidity: String,
    var clouds: String,
    var DailyData: List<DailyData>
)

data class HourlyData(var temp: String, var time: String)
data class DailyData(var day: String, var date: String, var temp: String)