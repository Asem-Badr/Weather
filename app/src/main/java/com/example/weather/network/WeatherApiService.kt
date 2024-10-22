package com.example.weather.network

import com.example.weather.model.CurrentWeatherResponse
import com.example.weather.model.ForecastResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("lang") language: String,
        @Query("units") units: String,
        @Query("appid") apiKey: String
    ): CurrentWeatherResponse

    @GET("forecast")
    suspend fun getFiveDayForecast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("lang") language: String,
        @Query("units") units: String,
        @Query("appid") apiKey: String
    ): ForecastResponse

    object RetrofitHelper {
        private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

        val retrofitInstance: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val weatherService: WeatherApiService by lazy {
            retrofitInstance.create(WeatherApiService::class.java)
        }
    }
}
