package com.example.weather.ui.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.viewModels
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import android.widget.RadioButton
import com.example.weather.R
import com.example.weather.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        const val PREFERENCES_NAME = "weather_preferences"
        const val LANGUAGE_KEY = "language"
        const val TEMP_UNIT_KEY = "temperature_unit"
        const val LOCATION_KEY = "location"
        const val WIND_SPEED_KEY = "wind_speed_unit"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        sharedPreferences = requireActivity().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

        loadPreferences()

        binding.languageGroup.setOnCheckedChangeListener { _, checkedId ->
            val selectedLanguage = when (checkedId) {
                R.id.radioButtonEnglish -> "en"
                R.id.radioButtonArabic -> "ar"
                else -> "en" // default to English
            }
            savePreference(LANGUAGE_KEY, selectedLanguage)
        }

        binding.tempUnitGroup.setOnCheckedChangeListener { _, checkedId ->
            val selectedTempUnit = when (checkedId) {
                R.id.radioButtonCelsius -> "Celsius"
                R.id.radioButtonKelvin -> "Kelvin"
                R.id.radioButtonFahrenheit -> "Fahrenheit"
                else -> "Celsius" // default to Celsius
            }
            savePreference(TEMP_UNIT_KEY, selectedTempUnit)
        }

        binding.locationGroup.setOnCheckedChangeListener { _, checkedId ->
            val selectedLocation = when (checkedId) {
                R.id.radioButtonGPS -> "GPS"
                R.id.radioButtonMap -> "Map"
                else -> "GPS" // default to GPS
            }
            savePreference(LOCATION_KEY, selectedLocation)
        }

        binding.windSpeedGroup.setOnCheckedChangeListener { _, checkedId ->
            val selectedWindSpeedUnit = when (checkedId) {
                R.id.radioButtonMetersPerSec -> "Meters/Sec"
                R.id.radioButtonMilesPerHour -> "Miles/Hour"
                else -> "Meters/Sec" // default to Meters/Sec
            }
            savePreference(WIND_SPEED_KEY, selectedWindSpeedUnit)
        }

        return root
    }

    private fun loadPreferences() {
        val savedLanguage = sharedPreferences.getString(LANGUAGE_KEY, "en")
        val savedTempUnit = sharedPreferences.getString(TEMP_UNIT_KEY, "Celsius")
        val savedLocation = sharedPreferences.getString(LOCATION_KEY, "GPS")
        val savedWindSpeedUnit = sharedPreferences.getString(WIND_SPEED_KEY, "Meters/Sec")

        binding.languageGroup.check(
            when (savedLanguage) {
                "ar" -> R.id.radioButtonArabic
                "en" -> R.id.radioButtonEnglish
                else -> R.id.radioButtonEnglish
            }
        )

        binding.tempUnitGroup.check(
            when (savedTempUnit) {
                "Celsius" -> R.id.radioButtonCelsius
                "Kelvin" -> R.id.radioButtonKelvin
                "Fahrenheit" -> R.id.radioButtonFahrenheit
                else -> R.id.radioButtonCelsius
            }
        )

        binding.locationGroup.check(
            when (savedLocation) {
                "GPS" -> R.id.radioButtonGPS
                "Map" -> R.id.radioButtonMap
                else -> R.id.radioButtonGPS
            }
        )
        binding.windSpeedGroup.check(
            when (savedWindSpeedUnit) {
                "Meters/Sec" -> R.id.radioButtonMetersPerSec
                "Miles/Hour" -> R.id.radioButtonMilesPerHour
                else -> R.id.radioButtonMetersPerSec
            }
        )
    }

    private fun savePreference(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
