package com.example.wetherapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.wetherapp.data.local.entity.ForecastEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ForecastDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecasts(forecasts: List<ForecastEntity>)
    
    @Query("SELECT * FROM forecast WHERE cityName = :cityName ORDER BY dt ASC")
    fun getForecastsByCity(cityName: String): Flow<List<ForecastEntity>>
    
    @Query("SELECT * FROM forecast ORDER BY timestamp DESC, dt ASC LIMIT 40")
    fun getLatestForecasts(): Flow<List<ForecastEntity>>
    
    @Query("DELETE FROM forecast WHERE cityName = :cityName")
    suspend fun deleteForecastsForCity(cityName: String)
    
    @Query("DELETE FROM forecast")
    suspend fun deleteAllForecasts()
} 