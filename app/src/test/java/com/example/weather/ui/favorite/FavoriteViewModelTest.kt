package com.example.weather.ui.favorite

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weather.model.DisplayableDailyForecast
import com.example.weather.model.DisplayableHourlyForecast
import com.example.weather.model.DisplayableWeatherData
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class FavoriteViewModelTest{
    @Test
    fun addToFav_LocationIsAdded() = runTest {
        // Given a FavoriteViewModel and a new location
        val fakeRepository = FakeRepository()
        val favoriteViewModel = FavoriteViewModel(fakeRepository)
        var sampleWeatherData = DisplayableWeatherData(
        weatherDescription = "Clear Sky",
        weatherIconUrl = "https://example.com/icons/clear_sky.png",
        currentDay = "Monday",
        currentDate = "2024-11-02",
        temperature = "22°C",
        locationDescription = "San Francisco, CA",
        pressure = "1015 hPa",
        windSpeed = "5 km/h",
        humidity = "60%",
        cloudCoverage = "10%",
        hourlyForecast = listOf(
            DisplayableHourlyForecast(time = "09:00", temp = "20°C", iconUrl = "https://example.com/icons/20_degrees.png"),
            DisplayableHourlyForecast(time = "12:00", temp = "22°C", iconUrl = "https://example.com/icons/22_degrees.png"),
            DisplayableHourlyForecast(time = "15:00", temp = "21°C", iconUrl = "https://example.com/icons/21_degrees.png")
        ),
        dailyForecast = listOf(
            DisplayableDailyForecast(day = "Monday", temp = "22°C", iconUrl = "https://example.com/icons/clear_sky.png"),
            DisplayableDailyForecast(day = "Tuesday", temp = "20°C", iconUrl = "https://example.com/icons/partly_cloudy.png"),
            DisplayableDailyForecast(day = "Wednesday", temp = "18°C", iconUrl = "https://example.com/icons/rain.png")
        ),
        latitude = 37.7749,
        longitude = -122.4194,
        timeStamp = System.currentTimeMillis()
    )

        // When adding a location to favorites
        favoriteViewModel.addToFav(sampleWeatherData)

        // Then the location is added and locations LiveData updates
       favoriteViewModel.locations.observeForever{ result ->
            assertTrue(result.contains(sampleWeatherData))
        }
    }
    @Test
    fun removeFromFav_LocationIsRemoved() = runTest {
        val fakeRepository = FakeRepository()
        val favoriteViewModel = FavoriteViewModel(fakeRepository)
        // Given a location that has been added to favorites
        val sampleWeatherData = DisplayableWeatherData(
            weatherDescription = "Clear Sky",
            weatherIconUrl = "https://example.com/icons/clear_sky.png",
            currentDay = "Monday",
            currentDate = "2024-11-02",
            temperature = "22°C",
            locationDescription = "San Francisco, CA",
            pressure = "1015 hPa",
            windSpeed = "5 km/h",
            humidity = "60%",
            cloudCoverage = "10%",
            hourlyForecast = listOf(
                DisplayableHourlyForecast(time = "09:00", temp = "20°C", iconUrl = "https://example.com/icons/20_degrees.png"),
                DisplayableHourlyForecast(time = "12:00", temp = "22°C", iconUrl = "https://example.com/icons/22_degrees.png"),
                DisplayableHourlyForecast(time = "15:00", temp = "21°C", iconUrl = "https://example.com/icons/21_degrees.png")
            ),
            dailyForecast = listOf(
                DisplayableDailyForecast(day = "Monday", temp = "22°C", iconUrl = "https://example.com/icons/clear_sky.png"),
                DisplayableDailyForecast(day = "Tuesday", temp = "20°C", iconUrl = "https://example.com/icons/partly_cloudy.png"),
                DisplayableDailyForecast(day = "Wednesday", temp = "18°C", iconUrl = "https://example.com/icons/rain.png")
            ),
            latitude = 37.7749,
            longitude = -122.4194,
            timeStamp = System.currentTimeMillis()
        )

        // Add the location to favorites first
        favoriteViewModel.addToFav(sampleWeatherData)

        // Now, remove the location from favorites
        favoriteViewModel.removeFromFav(sampleWeatherData)

        // Observe the locations LiveData and assert that the location has been removed
        favoriteViewModel.locations.observeForever { result ->
            assertTrue(!result.contains(sampleWeatherData))
        }
    }

}