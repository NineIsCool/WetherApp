package com.example.wetherapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Parcelize
data class Weather(
    val id: Long,
    val cityName: String,
    val countryCode: String,
    val lat: Double,
    val lon: Double,
    val temperature: Double,
    val feelsLike: Double,
    val tempMin: Double,
    val tempMax: Double,
    val pressure: Int,
    val humidity: Int,
    val windSpeed: Double,
    val windDeg: Int,
    val weatherMain: String,
    val weatherDescription: String,
    val weatherIcon: String,
    val visibility: Int,
    val dt: Long,
    val timezone: Int,
    val sunrise: Long,
    val sunset: Long
) : Parcelable {
    
    fun getFormattedTime(timestamp: Long): String {
        val formatter = SimpleDateFormat("H:mm", Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("GMT")
        val offsetMillis = timezone * 1000L
        return formatter.format(Date(timestamp * 1000L + offsetMillis))
    }
    
    fun getCurrentTime(): String {
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        formatter.timeZone = TimeZone.getDefault()
        return formatter.format(Date())
    }
    
    fun getIconUrl(): String = "https://openweathermap.org/img/wn/${weatherIcon}@2x.png"
    
    fun getWindDirection(): String {
        val directions = arrayOf("N", "NE", "E", "SE", "S", "SW", "W", "NW", "N")
        return directions[(windDeg / 45) % 8]
    }
} 