package com.example.wetherapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Parcelize
data class Forecast(
    val id: String,
    val cityId: Long,
    val cityName: String,
    val dt: Long,
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
    val dtTxt: String
) : Parcelable {
    
    fun getIconUrl(): String = "https://openweathermap.org/img/wn/${weatherIcon}@2x.png"
    
    fun getFormattedDay(): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        val date = inputFormat.parse(dtTxt)
        return if (isToday(dtTxt)) "Today" else date?.let { outputFormat.format(it) } ?: ""
    }
    
    fun getFormattedTime(): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
        val date = inputFormat.parse(dtTxt)
        return date?.let { outputFormat.format(it) } ?: ""
    }
    
    fun getFormattedDate(): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMM d", Locale.getDefault())
        val date = inputFormat.parse(dtTxt)
        return date?.let { outputFormat.format(it) } ?: ""
    }
    
    private fun isToday(dateString: String): Boolean {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        return dateString.startsWith(today)
    }
    
    fun getWindDirection(): String {
        val directions = arrayOf("N", "NE", "E", "SE", "S", "SW", "W", "NW")
        val index = ((windDeg % 360) / 45)
        return directions[index]
    }
} 