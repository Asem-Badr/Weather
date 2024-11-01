package com.example.weather.ui.home

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weather.model.CurrentWeatherResponse
import com.example.weather.model.DisplayableDailyForecast
import com.example.weather.model.DisplayableHourlyForecast
import com.example.weather.model.DisplayableWeatherData
import com.example.weather.model.ForecastResponse
import com.example.weather.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class HomeViewModel(private val _repo: Repository) : ViewModel() {

    private var _currentWeatherResponse: MutableLiveData<CurrentWeatherResponse> =
        MutableLiveData<CurrentWeatherResponse>()
    val currentWeatherResponse: LiveData<CurrentWeatherResponse> = _currentWeatherResponse

    private var _forecastResponse: MutableLiveData<ForecastResponse> =
        MutableLiveData<ForecastResponse>()
    val forecastResponse: LiveData<ForecastResponse> = _forecastResponse

    private var _favorite_object: MutableLiveData<DisplayableWeatherData> =
        MutableLiveData<DisplayableWeatherData>()
    val favoriteObject: LiveData<DisplayableWeatherData> = _favorite_object

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
    fun getLocationByDescription(location : String){
        CoroutineScope(Dispatchers.IO).launch {
            val result = _repo.searchForWeather(location)
                .collect{
                    _favorite_object.postValue(it)
                }
        }
    }
    fun addToFav(location: DisplayableWeatherData){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.addToFav(location)
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getDisplayableObject(lat: Double, lon: Double, apiKey: String): DisplayableWeatherData =
        coroutineScope {
            val currentWeatherDeferred = async {
                _repo.getCurrentWeather(lat, lon, apiKey).first()
            }

            val forecastDeferred = async {
                _repo.getFiveDayForecast(lat, lon, apiKey).first()
            }

            val currentWeather = currentWeatherDeferred.await()
            val forecastResponse = forecastDeferred.await()
            constructDisplayableWeatherData(currentWeather,forecastResponse)

        }
    @RequiresApi(Build.VERSION_CODES.O)
    fun constructDisplayableWeatherData(currentWeather: CurrentWeatherResponse, forecastResponse : ForecastResponse):DisplayableWeatherData{
        val dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMM d")
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        val currentDay = ZonedDateTime.now(ZoneId.systemDefault()).format(dateFormatter)
        val tempUnit = _repo.settingsManager.getTemperatureUnit().first()
        var windSpeedUnit = _repo.settingsManager.getWindSpeedUnit()
        windSpeedUnit = if(windSpeedUnit == "Meters/Sec") "M/S" else "Mile/H"
        if(_repo.settingsManager.getLanguage()=="ar"){
            if(windSpeedUnit == "M/S")windSpeedUnit="م/ث" else windSpeedUnit="ميل/س"
        }
        val weatherDescription = currentWeather.weather.firstOrNull()?.description.orEmpty()
        val weatherIconUrl = "https://openweathermap.org/img/wn/${currentWeather.weather.firstOrNull()?.icon}@2x.png"
        val locationDescription = "${currentWeather.name}, ${currentWeather.sys.country}"
        val temperature = "${currentWeather.main.temp} °"+tempUnit
        var pressureUnit = if(_repo.settingsManager.getLanguage()=="ar")"مللي بار" else "hPa"
        val pressure = "${currentWeather.main.pressure}"+pressureUnit
        val windSpeed = "${currentWeather.wind.speed} "+ windSpeedUnit
        val humidity = "${currentWeather.main.humidity}%"
        val cloudCoverage = "${currentWeather.clouds.all}%"
        val currentDayStart = ZonedDateTime.now(ZoneId.systemDefault()).toLocalDate()
        val hourlyForecast = forecastResponse.list
            //.filter { ZonedDateTime.ofInstant(Instant.ofEpochSecond(it.dt), ZoneId.systemDefault()).toLocalDate() == currentDayStart }
            .take(8)
            .map {
                DisplayableHourlyForecast(
                    time = ZonedDateTime.ofInstant(Instant.ofEpochSecond(it.dt), ZoneId.systemDefault()).format(timeFormatter),
                    temp = "${it.main.temp} °"+tempUnit,
                    iconUrl = "https://openweathermap.org/img/wn/${it.weather.firstOrNull()?.icon}@2x.png"
                )
            }

        val dailyForecast = forecastResponse.list
            .filterIndexed { index, _ -> index % 8 == 0 }
            .map {
                val dateTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(it.dt), ZoneId.systemDefault())
                DisplayableDailyForecast(
                    day = dateTime.format(dateFormatter),
                    temp = "${it.main.temp} °"+tempUnit,
                    iconUrl = "https://openweathermap.org/img/wn/${it.weather.firstOrNull()?.icon}@2x.png"
                )
            }

        return DisplayableWeatherData(
            weatherDescription = weatherDescription,
            weatherIconUrl = weatherIconUrl,
            currentDay = "Today",
            currentDate = currentDay,
            temperature = temperature,
            locationDescription = locationDescription,
            pressure = pressure,
            windSpeed = windSpeed,
            humidity = humidity,
            cloudCoverage = cloudCoverage,
            hourlyForecast = hourlyForecast,
            dailyForecast = dailyForecast,
            latitude = currentWeather.coord.lat,
            longitude = currentWeather.coord.lon,
            timeStamp = currentWeather.dt
        )
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