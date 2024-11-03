package com.example.weather


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.productslist.db.Dao
import com.example.weather.database.LocationsDatabase
import com.example.weather.model.DisplayableDailyForecast
import com.example.weather.model.DisplayableHourlyForecast
import com.example.weather.model.DisplayableWeatherData
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class LocalDataSourceTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: LocationsDatabase
    private lateinit var dao: Dao
    private lateinit var localDataSource: LocalDataSource

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            LocationsDatabase::class.java
        ).allowMainThreadQueries()
            .build()

        // Get a reference to the DAO
        dao = database.getLocationsDao()

        // Initialize the local data source
        localDataSource = LocalDataSource(dao)
    }

    @After
    fun tearDown() {
        // Close the database after each test
        database.close()
    }

    @Test
    fun saveWeather_retrieveWeather() = runTest {
        // Step 1: Create a weather data dto
        var sampleWeatherData1 = DisplayableWeatherData(
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

        // Step 2: Save the task
        localDataSource.insertLocation(sampleWeatherData1)

        // Step 3: Retrieve the task
        val result = localDataSource.getLocationByDescription(sampleWeatherData1.weatherDescription)

        // Step 4: Check if the task was retrieved correctly
        assertThat(result, `is`(sampleWeatherData1))
    }

    @Test
    fun deleteWeatherLocation_retrieveDeletedLocation() = runTest {
        // Step 1: Create a weather data dto
        val sampleWeatherData2 = DisplayableWeatherData(
            weatherDescription = "Rainy",
            weatherIconUrl = "https://example.com/icons/rainy.png",
            currentDay = "Tuesday",
            currentDate = "2024-11-03",
            temperature = "18°C",
            locationDescription = "Seattle, WA",
            pressure = "1010 hPa",
            windSpeed = "10 km/h",
            humidity = "70%",
            cloudCoverage = "90%",
            hourlyForecast = listOf(
                DisplayableHourlyForecast(time = "09:00", temp = "17°C", iconUrl = "https://example.com/icons/17_degrees.png"),
                DisplayableHourlyForecast(time = "12:00", temp = "18°C", iconUrl = "https://example.com/icons/18_degrees.png"),
                DisplayableHourlyForecast(time = "15:00", temp = "17°C", iconUrl = "https://example.com/icons/17_degrees.png")
            ),
            dailyForecast = listOf(
                DisplayableDailyForecast(day = "Tuesday", temp = "18°C", iconUrl = "https://example.com/icons/rainy.png"),
                DisplayableDailyForecast(day = "Wednesday", temp = "16°C", iconUrl = "https://example.com/icons/rain.png"),
                DisplayableDailyForecast(day = "Thursday", temp = "15°C", iconUrl = "https://example.com/icons/partly_cloudy.png")
            ),
            latitude = 47.6062,
            longitude = -122.3321,
            timeStamp = System.currentTimeMillis()
        )

        // Step 2: Save the weather location
        localDataSource.insertLocation(sampleWeatherData2)

        // Step 3: Delete the location
        localDataSource.deleteLocation(sampleWeatherData2)

        // Step 4: Attempt to retrieve the deleted location
        val result = localDataSource.getLocationByDescription(sampleWeatherData2.weatherDescription)

        // Step 5: Check if the location was deleted correctly (should be null)
        assertNull(result)
    }

}
