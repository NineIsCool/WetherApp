package com.example.wetherapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "forecast")
data class ForecastEntity(
    @PrimaryKey
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
    val dtTxt: String,
    val timestamp: Long = System.currentTimeMillis()
) 