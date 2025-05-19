package com.example.wetherapp.domain.repository

import com.example.wetherapp.domain.model.Forecast
import com.example.wetherapp.domain.model.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getCurrentWeather(lat: Double, lon: Double): Result<Weather>
    suspend fun getCurrentWeatherByCity(cityName: String): Result<Weather>
    suspend fun getForecast(lat: Double, lon: Double): Result<List<Forecast>>
    suspend fun getForecastByCity(cityName: String): Result<List<Forecast>>
    fun getLocalWeather(): Flow<Weather?>
    fun getLocalWeatherByCity(cityName: String): Flow<Weather?>
    fun getLocalForecasts(): Flow<List<Forecast>>
    fun getLocalForecastsByCity(cityName: String): Flow<List<Forecast>>
    suspend fun saveLastLocation(lat: Double, lon: Double, cityName: String)
    suspend fun getLastLocation(): Triple<Double, Double, String>
} 