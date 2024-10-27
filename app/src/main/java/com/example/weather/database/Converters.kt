package com.example.weather.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.weather.model.DisplayableHourlyForecast
import com.example.weather.model.DisplayableDailyForecast

class Converters {

    @TypeConverter
    fun fromHourlyForecastList(value: List<DisplayableHourlyForecast>?): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toHourlyForecastList(value: String): List<DisplayableHourlyForecast>? {
        val listType = object : TypeToken<List<DisplayableHourlyForecast>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromDailyForecastList(value: List<DisplayableDailyForecast>?): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toDailyForecastList(value: String): List<DisplayableDailyForecast>? {
        val listType = object : TypeToken<List<DisplayableDailyForecast>>() {}.type
        return Gson().fromJson(value, listType)
    }
}
