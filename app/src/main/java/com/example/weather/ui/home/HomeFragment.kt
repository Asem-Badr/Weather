package com.example.weather.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weather.MyLocationManager
import com.example.weather.SettingsManager
import com.example.weather.databinding.FragmentHomeBinding
import com.example.weather.network.WeatherApiService
import com.example.weather.repository.Repository

class HomeFragment : Fragment(), LocationResultCallback {

    val isAvailable = false;
    lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    lateinit var myLocationManager: MyLocationManager
    private val binding get() = _binding!!
    override fun onStart() {
        super.onStart()
        myLocationManager.getActualLocation(requireActivity(), this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val settingsManager = SettingsManager(requireActivity())
        val homeViewModelFactory =
            HomeViewModelFactory(Repository(WeatherApiService.RetrofitHelper, settingsManager))
        homeViewModel = ViewModelProvider(
            requireActivity(),
            homeViewModelFactory
        ).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        if (settingsManager.getLocation() == "GPS") {
            myLocationManager = MyLocationManager(requireContext())
        }

//        homeViewModel.getCurrentWeather(lat = 10.0, lon = 10.0, "fe475ba8548cc787edbdab799cae490c")
//        homeViewModel.getFiveDayForecast(lat = 10.0, lon = 10.0, "fe475ba8548cc787edbdab799cae490c")

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onLocationResult(latitude: Double, longitude: Double) {
        Toast.makeText(requireContext(), "long: ${longitude}, lat:${latitude}", Toast.LENGTH_SHORT)
            .show()
        homeViewModel.getCurrentWeather(latitude, longitude, "fe475ba8548cc787edbdab799cae490c")
        homeViewModel.getFiveDayForecast(latitude , longitude, "fe475ba8548cc787edbdab799cae490c")

        homeViewModel.currentWeatherResponse.observe(viewLifecycleOwner, Observer {
            Log.i("TAG", "onCreateView: " + "hey there!")
            Toast.makeText(
                requireActivity(),
                "" + it.weather.get(0).description,
                Toast.LENGTH_SHORT
            ).show()
            binding.txtTemp.text = it.main.temp.toString()
        })
        homeViewModel.forecastResponse.observe(viewLifecycleOwner, Observer {

        })
    }

}