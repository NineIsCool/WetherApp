package com.example.wetherapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey
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
) 