package com.example.weather.ui.alerts

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

import com.example.weather.R

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // Retrieve weather information from intent
        val description = intent.getStringExtra("weather_description") ?: "No description available"
        val temperature = intent.getStringExtra("temperature") ?: "N/A"

        // Show notification with weather details
        showNotification(context, description, temperature)
    }

    private fun showNotification(context: Context, description: String, temperature: String) {
        val notificationId = 1
        val channelId = "alarm_channel_id"

        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notifications_black_24dp)
            .setContentTitle("Current Weather")
            .setContentText("Weather: $description, Temp: $temperature°")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}

