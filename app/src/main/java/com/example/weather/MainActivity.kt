package com.example.weather

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.weather.databinding.ActivityMainBinding
import com.example.weather.network.WeatherApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Remove this line, no need to set up action bar:
        // setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // You can keep this if you want to hide the action bar programmatically:
        supportActionBar?.hide()
        fetchWeatherData()
    }


}

private fun fetchWeatherData() {
    // Launch the coroutine in IO context to make the network call
    CoroutineScope(Dispatchers.IO).launch {
        val lat = 44.34
        val lon = 10.99
        val apiKey = "my key goes here "
        val language = "ar"
        val units = "metric"

        try {
            // Make the API call to get current weather
            val currentWeather = WeatherApiService.RetrofitHelper.weatherService.getCurrentWeather(
                lat, lon, language, units, apiKey
            )
            val forecast = WeatherApiService.RetrofitHelper.weatherService.getFiveDayForecast(
                lat,
                lon,
                "ar",
                "metric",
                apiKey
            )

            // If successful, do something with the result, e.g., update UI
            CoroutineScope(Dispatchers.Main).launch {
                // Update the UI in the main thread
                Log.i(
                    "TAG",
                    "fetchWeatherData: " + "Weather: ${currentWeather.weather[0].description}"
                )
                Log.i(
                    "TAG",
                    "fetchWeatherData: " + "fiveDaysForecast : ${forecast.list.get(0).weather}"
                )
            }

        } catch (e: IOException) {
            // Handle network issues
            println("Network Error: ${e.message}")
        } catch (e: HttpException) {
            // Handle invalid response (e.g., 404 or 500)
            println("HTTP Error: ${e.message}")
        }
    }
}