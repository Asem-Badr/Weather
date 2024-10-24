package com.example.weather.ui.home

interface LocationResultCallback {
    fun onLocationResult(latitude: Double, longitude: Double)
}