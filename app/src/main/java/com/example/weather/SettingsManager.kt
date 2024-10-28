package com.example.weather

import android.content.Context
import android.content.SharedPreferences

class SettingsManager(context: Context) {

    companion object {
        const val PREFERENCES_NAME = "weather_preferences"
        const val LANGUAGE_KEY = "language"
        const val TEMP_UNIT_KEY = "temperature_unit"
        const val LOCATION_KEY = "location"
        const val WIND_SPEED_KEY = "wind_speed_unit"
        const val UNIT_KEY = "unit"
    }

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun getLanguage(): String {
        return sharedPreferences.getString(LANGUAGE_KEY, "en") ?: "en"
    }

    fun setLanguage(language: String) {
        sharedPreferences.edit().putString(LANGUAGE_KEY, language).apply()
    }

    fun getTemperatureUnit(): String {
        return sharedPreferences.getString(TEMP_UNIT_KEY, "Celsius") ?: "Celsius"
    }

    fun setTemperatureUnit(tempUnit: String) {
        sharedPreferences.edit().putString(TEMP_UNIT_KEY, tempUnit).apply()
    }

    fun getLocation(): String {
        return sharedPreferences.getString(LOCATION_KEY, "GPS") ?: "GPS"
    }

    fun setLocation(location: String) {
        sharedPreferences.edit().putString(LOCATION_KEY, location).apply()
    }

    fun getWindSpeedUnit(): String {
        return sharedPreferences.getString(WIND_SPEED_KEY, "Meters/Sec") ?: "Meters/Sec"
    }

    fun setWindSpeedUnit(windSpeedUnit: String) {
        sharedPreferences.edit().putString(WIND_SPEED_KEY, windSpeedUnit).apply()
    }

    fun getUnit(): String{
        return sharedPreferences.getString(UNIT_KEY,"metric") ?: "metric"
    }

    fun setUnit(unit : String){
        sharedPreferences.edit().putString(UNIT_KEY,unit).apply()
    }
}
