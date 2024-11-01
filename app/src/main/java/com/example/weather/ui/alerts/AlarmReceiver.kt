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
        // Show notification when the alarm is triggered
        showNotification(context)
    }

    private fun showNotification(context: Context) {
        val notificationId = 1
        val channelId = "alarm_channel_id"

        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notifications_black_24dp)  // Use a suitable icon
            .setContentTitle("Alarm Alert")
            .setContentText("The alarm you set has gone off!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission not granted, do not proceed with the notification
            return
        }
        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}
