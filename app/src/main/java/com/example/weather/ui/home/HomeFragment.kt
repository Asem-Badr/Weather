package com.example.weather.ui.home

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weather.database.LocationsDatabase
import com.example.weather.MyLocationManager
import com.example.weather.SettingsManager
import com.example.weather.databinding.FragmentHomeBinding
import com.example.weather.model.DisplayableWeatherData
import com.example.weather.network.WeatherApiService
import com.example.weather.repository.Repository
import com.google.gson.Gson
import kotlinx.coroutines.launch

class HomeFragment : Fragment(), LocationResultCallback {
    lateinit var recyclerAdapterHourlyData: RecyclerAdapterHourlyData
    lateinit var recyclerViewHourlyData: RecyclerView
    lateinit var recyclerAdapterDailyData: RecyclerAdapterDailyData
    lateinit var recyclerViewDailyData: RecyclerView
    lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    lateinit var myLocationManager : MyLocationManager
    private val binding get() = _binding!!
    lateinit var settingsManager :SettingsManager

    override fun onStart() {
        super.onStart()
//        myLocationManager.getActualLocation(requireActivity(), this)
        myLocationManager = MyLocationManager(requireContext())
//        if(myLocationManager.getGpsLongitude()== "0"){
//            myLocationManager.getFreshLocation(this)
//        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        refreshData()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        settingsManager = SettingsManager(requireActivity())
        val homeViewModelFactory =
            HomeViewModelFactory(
                Repository(
                    WeatherApiService.RetrofitHelper,
                    LocationsDatabase.getInstance(requireContext()).getLocationsDao(),
                    settingsManager
                )
            )
        homeViewModel = ViewModelProvider(
            requireActivity(),
            homeViewModelFactory
        ).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        recyclerViewDailyData = binding.recyclerViewDaily
        recyclerViewHourlyData = binding.recyclerViewHourly

        recyclerAdapterHourlyData = RecyclerAdapterHourlyData()
        recyclerViewHourlyData.apply {
            adapter = recyclerAdapterHourlyData
            layoutManager = LinearLayoutManager(requireContext()).apply {
                orientation = RecyclerView.HORIZONTAL
            }
        }

        recyclerAdapterDailyData = RecyclerAdapterDailyData()
        recyclerViewDailyData.apply {
            adapter = recyclerAdapterDailyData
            layoutManager = LinearLayoutManager(requireContext()).apply {
                orientation = RecyclerView.VERTICAL
            }
        }


        return root
    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onLocationResult(latitude: Double, longitude: Double) {
//        Toast.makeText(requireContext(), "long: ${longitude}, lat:${latitude}", Toast.LENGTH_SHORT) .show()
        lifecycleScope.launch {
            var value = homeViewModel.getDisplayableObject(
                latitude,
                longitude,
                "fe475ba8548cc787edbdab799cae490c"
            )
            var location_settings = settingsManager.getLocation()
            if(location_settings == "GPS"){
                //caching the locations so that it will not fetch new location every time
                myLocationManager.setGpsLongitude(longitude)
                myLocationManager.setGpsLatitude(latitude)
            }else if (location_settings == "Map"){

            }
            updateUi(value)
            //just for testing not production
//            val settingsManager = SettingsManager(requireActivity())
//            val repo = Repository(
//                WeatherApiService.RetrofitHelper,
//                LocationsDatabase.getInstance(requireContext()).getLocationsDao(),
//                settingsManager
//            )
            //this is for debug only
            //repo.addToFav(value)
        }

    }

    fun updateUi(dto: DisplayableWeatherData) {
        binding.apply {
            // Set temperature and weather description
            txtTemp.text = dto.temperature
            txtWeatherDesc.text = dto.weatherDescription

            // Set location, date, and other details
            txtLocationDesc.text = dto.locationDescription
            txtDay.text = dto.currentDay
            txtDate.text = dto.currentDate
            txtPressure.text = dto.pressure
            txtWindSpeed.text = dto.windSpeed
            txtHumidity.text = dto.humidity
            txtClouds.text = dto.cloudCoverage

            // Load the weather icon using an image loading library
            Glide.with(root.context)
                .load(dto.weatherIconUrl)
                .into(imageView)

            // Update RecyclerViews with adapters for hourly and daily data
            recyclerAdapterHourlyData.submitList(dto.hourlyForecast)
            recyclerAdapterDailyData.submitList(dto.dailyForecast)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun refreshData() {
        myLocationManager = MyLocationManager(requireContext())
        val locationSetting = settingsManager.getLocation()

        when (locationSetting) {
            "GPS" -> {
                if (myLocationManager.getGpsLongitude() != "0") {
                    onLocationResult(
                        myLocationManager.getGpsLatitude().toDouble(),
                        myLocationManager.getGpsLongitude().toDouble()
                    )
                } else {
                    myLocationManager.getFreshLocation(this)  // Only fetch fresh GPS if needed
                }
            }
            "Map" -> onLocationResult(
                myLocationManager.getLatitude().toDouble(),
                myLocationManager.getLongitude().toDouble()
            )
            "Favorite" -> {
                settingsManager.setLocation("GPS")
                val weatherKey = myLocationManager.getWeatherObject()
                homeViewModel.getLocationByDescription(weatherKey)
                homeViewModel.favoriteObject.observe(viewLifecycleOwner) {
                    updateUi(it)
                    homeViewModel.addToFav(it)
                }
            }
        }
    }
}