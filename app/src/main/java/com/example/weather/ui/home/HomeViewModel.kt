package com.example.weather.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weather.MyLocationManager
import com.example.weather.model.CurrentWeatherResponse
import com.example.weather.model.DisplayableWeatherData
import com.example.weather.model.ForecastResponse
import com.example.weather.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private val _repo: Repository) : ViewModel() {
    //lateinit var myLocationManager: MyLocationManager

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

    // moving this logic here requires context
//    fun getUserLocationSelection(){
//        if (_repo.settingsManager.getLocation() == "GPS") {
//            myLocationManager = MyLocationManager()
//        }
//    }

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

//    fun getDisplayableObject(lat: Double, lon: Double, apiKey: String):DisplayableWeatherData{
//        CoroutineScope(Dispatchers.IO).launch {
//            getCurrentWeather(lat, lon, apiKey)
//            getFiveDayForecast(lat, lon, apiKey)
//            val resultCurrent = currentWeatherResponse.value
//            val resultForecast = forecastResponse.value
//        }
//        return DisplayableWeatherData(forecastResponse.weather.get(0).description,
//            )
//    }
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