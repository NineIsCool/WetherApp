package com.example.wetherapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.wetherapp.data.local.entity.WeatherEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherEntity)
    
    @Query("SELECT * FROM weather WHERE cityName = :cityName LIMIT 1")
    fun getWeatherByCity(cityName: String): Flow<WeatherEntity?>
    
    @Query("SELECT * FROM weather ORDER BY dt DESC LIMIT 1")
    fun getLatestWeather(): Flow<WeatherEntity?>
    
    @Query("DELETE FROM weather")
    suspend fun deleteAllWeather()
} 