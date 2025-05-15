package com.example.wetherapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.wetherapp.data.local.dao.ForecastDao
import com.example.wetherapp.data.local.dao.WeatherDao
import com.example.wetherapp.data.local.entity.ForecastEntity
import com.example.wetherapp.data.local.entity.WeatherEntity

@Database(
    entities = [WeatherEntity::class, ForecastEntity::class],
    version = 3,
    exportSchema = false
)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
    abstract fun forecastDao(): ForecastDao
} 