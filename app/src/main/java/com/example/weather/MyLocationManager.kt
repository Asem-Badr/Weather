package com.example.weather

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.WRITE_SETTINGS
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Looper
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.weather.ui.home.LocationResultCallback
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority


import com.google.android.gms.location.FusedLocationProviderClient

class MyLocationManager(val context: Context) {

    companion object {
        const val PREFERENCES_NAME = "location_preferences"
        const val LONGITUDE_KEY = "longitude_key"
        const val LATITUDE_KEY = "latitude_key"
        const val WEATHER_OBJECT_KEY = "weather"
        const val GPS_LONG_KEY = "gps_longitude_key"
        const val GPS_LAT_KEY = "gps_latitude_key"
        const val Favorite_LONG_KEY = "favorite_longitude_key"
        const val Favorite_LAT_KEY = "favorite_latitude_key"
    }

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun getLongitude(): String {
        return sharedPreferences.getString(LONGITUDE_KEY, "0") ?: "0"
    }

    fun setLongitude(longitude: Double) {
        sharedPreferences.edit().putString(LONGITUDE_KEY, longitude.toString()).apply()
    }
    fun getLatitude(): String {
        return sharedPreferences.getString(LATITUDE_KEY, "0") ?: "0"
    }

    fun setGpsLongitude(longitude: Double) {
        sharedPreferences.edit().putString(GPS_LONG_KEY, longitude.toString()).apply()
    }
    fun getGpsLongitude(): String {
        return sharedPreferences.getString(GPS_LONG_KEY, "0") ?: "0"
    }

    fun setGpsLatitude(latitude: Double) {
        sharedPreferences.edit().putString(GPS_LAT_KEY, latitude.toString()).apply()
    }
    fun getGpsLatitude(): String {
        return sharedPreferences.getString(GPS_LAT_KEY, "0") ?: "0"
    }

    fun setFavoriteLongitude(longitude: Double) {
        sharedPreferences.edit().putString(Favorite_LONG_KEY, longitude.toString()).apply()
    }
    fun getFavoriteLongitude(): String {
        return sharedPreferences.getString(Favorite_LONG_KEY, "0") ?: "0"
    }

    fun setFavoriteLatitude(latitude: Double) {
        sharedPreferences.edit().putString(Favorite_LAT_KEY, latitude.toString()).apply()
    }
    fun getFavoriteLatitude(): String {
        return sharedPreferences.getString(Favorite_LAT_KEY, "0") ?: "0"
    }

    fun setLatitude(latitude: Double) {
        sharedPreferences.edit().putString(LATITUDE_KEY, latitude.toString()).apply()
    }

    fun getWeatherObject():String{
        return sharedPreferences.getString(WEATHER_OBJECT_KEY,"not found") ?:"not found"
    }

    fun setWeatherObject(weather:String){
        sharedPreferences.edit().putString(WEATHER_OBJECT_KEY, weather).apply()
    }
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    val My_LOCATION_PERMISSION_ID = 5005
    fun getActualLocation(activity: Activity, callback: LocationResultCallback) {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                getFreshLocation(callback)
            } else {
                enableLocationServices()
            }
        } else {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ),
                My_LOCATION_PERMISSION_ID
            )
        }
    }

    fun checkPermissions(): Boolean {
        var result = false
        if ((ContextCompat.checkSelfPermission(
                context,
                ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
            ||
            (ContextCompat.checkSelfPermission(
                context,
                ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
        ) {
            result = true;
        }
        return result
    }

    @SuppressLint("MissingPermission")
    fun getFreshLocation(callback: LocationResultCallback) {
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)

        fusedLocationProviderClient.requestLocationUpdates(
            LocationRequest.Builder(0).apply {
                setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            }.build(),
            object : LocationCallback() {
                override fun onLocationResult(location: LocationResult) {
                    super.onLocationResult(location)
                    val latitude = location.lastLocation?.latitude
                    val longitude = location.lastLocation?.longitude

                    if (latitude != null && longitude != null) {
                        // Use the callback to return the location
                        callback.onLocationResult(latitude, longitude)
                    }
                    fusedLocationProviderClient.removeLocationUpdates(this)
                }
            },
            Looper.myLooper()
        )
    }


    fun enableLocationServices() {
//        Toast.makeText(this,"Turn On Loction",Toast.LENGTH_SHORT).show()
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        context.startActivity(intent)
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
}