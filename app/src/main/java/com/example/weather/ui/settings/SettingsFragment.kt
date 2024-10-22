package com.example.weather.ui.settings

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.weather.R
import com.example.weather.SettingsManager
import com.example.weather.databinding.FragmentSettingsBinding
import java.util.Locale

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var settingsManager: SettingsManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        settingsManager = SettingsManager(requireContext())

        loadPreferences()

        binding.languageGroup.setOnCheckedChangeListener { _, checkedId ->
            val selectedLanguage = when (checkedId) {
                R.id.radioButtonEnglish -> "en"
                R.id.radioButtonArabic -> "ar"
                else -> "en" // default to English
            }
            settingsManager.setLanguage(selectedLanguage)
            setLocale(selectedLanguage)
        }

        binding.tempUnitGroup.setOnCheckedChangeListener { _, checkedId ->
            val selectedTempUnit = when (checkedId) {
                R.id.radioButtonCelsius -> "Celsius"
                R.id.radioButtonKelvin -> "Kelvin"
                R.id.radioButtonFahrenheit -> "Fahrenheit"
                else -> "Celsius" // default to Celsius
            }
            settingsManager.setTemperatureUnit(selectedTempUnit)
        }

        binding.locationGroup.setOnCheckedChangeListener { _, checkedId ->
            val selectedLocation = when (checkedId) {
                R.id.radioButtonGPS -> "GPS"
                R.id.radioButtonMap -> "Map"
                else -> "GPS" // default to GPS
            }
            settingsManager.setLocation(selectedLocation)
        }

        binding.windSpeedGroup.setOnCheckedChangeListener { _, checkedId ->
            val selectedWindSpeedUnit = when (checkedId) {
                R.id.radioButtonMetersPerSec -> "Meters/Sec"
                R.id.radioButtonMilesPerHour -> "Miles/Hour"
                else -> "Meters/Sec" // default to Meters/Sec
            }
            settingsManager.setWindSpeedUnit(selectedWindSpeedUnit)
        }

        return root
    }

    private fun loadPreferences() {
        val savedLanguage = settingsManager.getLanguage()
        val savedTempUnit = settingsManager.getTemperatureUnit()
        val savedLocation = settingsManager.getLocation()
        val savedWindSpeedUnit = settingsManager.getWindSpeedUnit()

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration()
        config.setLocale(locale)

        // Update the configuration for the current resources
        requireActivity().resources.updateConfiguration(config, requireActivity().resources.displayMetrics)

        // Restart the activity to apply the language change
        requireActivity().recreate()
    }

}
