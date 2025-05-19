package com.example.wetherapp.data.remote

import com.example.wetherapp.data.remote.model.ForecastResponse
import com.example.wetherapp.data.remote.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String
    ): WeatherResponse
    
    @GET("weather")
    suspend fun getCurrentWeatherByCity(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String
    ): WeatherResponse
    
    @GET("forecast")
    suspend fun getForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String
    ): ForecastResponse
    
    @GET("forecast")
    suspend fun getForecastByCity(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String
    ): ForecastResponse
} 