package com.example.weather.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weather.SettingsManager
import com.example.weather.model.CurrentWeatherResponse
import com.example.weather.model.ForecastResponse
import com.example.weather.network.WeatherApiService
import com.example.weather.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class HomeViewModel(private val _repo: Repository) : ViewModel() {
    //    private var _products : MutableLiveData<List<Product>> = MutableLiveData<List<Product>>()
//    val products : LiveData<List<Product>> = _products
    private var _currentWeatherResponse: MutableLiveData<CurrentWeatherResponse> =
        MutableLiveData<CurrentWeatherResponse>()
    val currentWeatherResponse: LiveData<CurrentWeatherResponse> = _currentWeatherResponse

    private var _forecastResponse: MutableLiveData<ForecastResponse> =
        MutableLiveData<ForecastResponse>()
    val forecastResponse: LiveData<ForecastResponse> = _forecastResponse

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    fun getCurrentWeather(lat: Double, lon: Double, apiKey: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = _repo.getCurrentWeather(lat, lon, apiKey)
                .collect {
                    _currentWeatherResponse.postValue(it)
                }
        }
    }

    fun getFiveDayForecast(lat: Double, lon: Double, apiKey: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = _repo.getFiveDayForecast(lat, lon, apiKey)
                .collect {
                    _forecastResponse.postValue(it)
                }
        }
    }

    fun fetchWeatherData() {
        CoroutineScope(Dispatchers.IO).launch {
            val lat = 44.34
            val lon = 10.99
            val apiKey = "fe475ba8548cc787edbdab799cae490c"
            val language = "ar"
            val units = "metric"
            try {
                // Make the API call to get current weather
                CoroutineScope(Dispatchers.Main).launch {
                    val result = _repo.getCurrentWeather(lat, lon, apiKey)
                        .collect {
                            Log.i("TAG", "fetchWeatherData: " + it.weather.get(0).description)
                        }
                    val result2 = _repo.getFiveDayForecast(lat, lon, apiKey)
                        .collect {
                            Log.i(
                                "TAG",
                                "fetchWeatherData: " + it.list.get(0).weather.get(0).description
                            )
                        }
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
}

class HomeViewModelFactory(private val _repo: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            HomeViewModel(_repo) as T
        } else {
            throw IllegalArgumentException("ViewModel class not found")
        }
    }
}