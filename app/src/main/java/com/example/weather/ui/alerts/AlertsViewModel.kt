package com.example.weather.ui.alerts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weather.model.CurrentWeatherResponse
import com.example.weather.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlertsViewModel(val _repo: Repository) : ViewModel() {

    private var _currentWeatherResponse: MutableLiveData<CurrentWeatherResponse> =
        MutableLiveData<CurrentWeatherResponse>()
    val currentWeatherResponse: LiveData<CurrentWeatherResponse> = _currentWeatherResponse

    fun getCurrentWeather(lat: Double, lon: Double, apiKey: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = _repo.getCurrentWeather(lat, lon, apiKey)
                .collect {
                    _currentWeatherResponse.postValue(it)
                }
        }
    }
}
class AlertsViewModelFactory(private val _repo: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AlertsViewModel::class.java)) {
            AlertsViewModel(_repo) as T
        } else {
            throw IllegalArgumentException("ViewModel class not found")
        }
    }
}