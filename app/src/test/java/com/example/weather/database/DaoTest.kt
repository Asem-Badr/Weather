package com.example.weather.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.productslist.db.Dao
import com.example.weather.model.DisplayableDailyForecast
import com.example.weather.model.DisplayableHourlyForecast
import com.example.weather.model.DisplayableWeatherData

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class DaoTest {
    lateinit var database: ToDoDatabase
    lateinit var Dao: Dao

    @get:Rule
    val rule = InstantTaskExecutorRule()



    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ToDoDatabase::class.java
        ).allowMainThreadQueries()
            .build()
        Dao = database.WeatherDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun getTaskByID_saveTask_retrieveTask() = runTest {
        //given
        val sampleWeatherData1 = DisplayableWeatherData(
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


        //when
        Dao.insert(sampleWeatherData1)
        val result = Dao.getLocationByDescription(sampleWeatherData1.locationDescription)

        //then
        result.collect{
            assertThat(it?.locationDescription, `is`(sampleWeatherData1.locationDescription))
        }
    }

    @Test
    fun updateTask_newData_retrieveSameData() = runTest {
        //given
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

        //when
        Dao.insert(sampleWeatherData1)
        //update the DTO in database by reinserting with data changed
        sampleWeatherData1.locationDescription = "updated_first"
        Dao.insert(sampleWeatherData1)
        val result = Dao.getLocationByDescription(sampleWeatherData1.locationDescription)

        //then
        result.collect{
            assertThat(it?.locationDescription, `is`(sampleWeatherData1.locationDescription))
        }
    }
}