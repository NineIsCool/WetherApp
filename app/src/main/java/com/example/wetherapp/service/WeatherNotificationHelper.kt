package com.example.wetherapp.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.wetherapp.MainActivity
import com.example.wetherapp.R
import com.example.wetherapp.domain.model.Weather

object WeatherNotificationHelper {
    private const val CHANNEL_ID = "weather_updates_channel"
    private const val NOTIFICATION_ID = 1

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Weather Updates",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Shows weather updates"
                enableVibration(true)
                enableLights(true)
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showNotification(context: Context, weather: Weather) {
        val contentText = buildString {
            append("${weather.temperature}°C")
            append(", ${weather.weatherDescription}")
            append("\nFeels like: ${weather.feelsLike}°C")
            append("\nHumidity: ${weather.humidity}%")
            append("\nWind: ${weather.windSpeed} m/s ${weather.getWindDirection()}")
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("${weather.cityName}, ${weather.countryCode}")
            .setContentText(contentText)
            .setStyle(NotificationCompat.BigTextStyle().bigText(contentText))
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(
                PendingIntent.getActivity(
                    context,
                    0,
                    Intent(context, MainActivity::class.java),
                    PendingIntent.FLAG_IMMUTABLE
                )
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
} 