package com.example.weather.ui.alerts

import android.Manifest
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weather.databinding.FragmentAlertsBinding
import java.util.Calendar
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.weather.MyLocationManager
import com.example.weather.SettingsManager
import com.example.weather.database.LocationsDatabase
import com.example.weather.network.WeatherApiService
import com.example.weather.repository.Repository
import com.example.weather.ui.home.HomeViewModel
import com.example.weather.ui.home.HomeViewModelFactory

class AlertsFragment : Fragment() {
    private var _binding: FragmentAlertsBinding? = null
    private val binding get() = _binding!!
    var REQUEST_CODE = 5005
    lateinit var alertsViewModel: AlertsViewModel
    lateinit var locationManager: MyLocationManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationManager = MyLocationManager(requireContext())
        val settingsManager = SettingsManager(requireActivity())
        val alertsViewModelFactory =
            AlertsViewModelFactory(
                Repository(
                    WeatherApiService.RetrofitHelper,
                    LocationsDatabase.getInstance(requireContext()).getLocationsDao(),
                    settingsManager
                )
            )
        alertsViewModel = ViewModelProvider(
            requireActivity(),
            alertsViewModelFactory
        ).get(AlertsViewModel::class.java)
        createNotificationChannel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        // Check for notification permission
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                REQUEST_CODE // Define this constant for your permission request
            )
        }



        _binding = FragmentAlertsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Set up click listener for FloatingActionButton to show TimePickerDialog
        binding.floatingActionButton2.setOnClickListener {
            showTimePickerDialog()
        }

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)
        val timePickerDialog =
            TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
                val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                Log.i("TAG", "showTimePickerDialog: $formattedTime")
                // Schedule the alarm for the selected time
                scheduleAlarm(selectedHour, selectedMinute)
            }, currentHour, currentMinute, true)
        timePickerDialog.show()
    }

    private fun scheduleAlarm(hour: Int, minute: Int) {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), AlarmReceiver::class.java)

        alertsViewModel.currentWeatherResponse.observe(viewLifecycleOwner) { weather ->
            val description = weather.weather[0].description
            val temperature = weather.main.temp

            // Pass weather details in the intent
            intent.putExtra("weather_description", description)
            intent.putExtra("temperature", temperature.toString())

            val pendingIntent = PendingIntent.getBroadcast(
                requireContext(),
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            // Set the alarm time
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                if (before(Calendar.getInstance())) add(Calendar.DAY_OF_YEAR, 1)
            }

            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if(isNetworkConnected()){
            alertsViewModel.getCurrentWeather(
                locationManager.getGpsLatitude().toDouble(),
                locationManager.getGpsLongitude().toDouble(),
                "fe475ba8548cc787edbdab799cae490c"
            )} else {
                Toast.makeText(
                    requireContext(),
                    "can't schedule alarms",
                    Toast.LENGTH_SHORT
                ).show()
            }
            alertsViewModel.currentWeatherResponse.observe(requireActivity()){
                val channelId = "alarm_channel_id"
                val channelName = it.name //"Alarm Notifications"
                val channelDescription = it.weather.get(0).description //"Notifications for scheduled alarms"
                val importance = NotificationManager.IMPORTANCE_HIGH
                val channel = NotificationChannel(channelId, channelName, importance).apply {
                    description = channelDescription
                }
                val notificationManager =
                    requireContext().getSystemService(NotificationManager::class.java)
                notificationManager.createNotificationChannel(channel)
            }

        }
    }
    private fun isNetworkConnected(): Boolean {
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo?.isConnectedOrConnecting == true
    }
}

// Rest of the code remains the same
