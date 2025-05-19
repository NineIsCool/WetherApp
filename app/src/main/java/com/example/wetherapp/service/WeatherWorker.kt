package com.example.wetherapp.service

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.wetherapp.domain.repository.WeatherRepository
import org.koin.java.KoinJavaComponent.inject

class WeatherWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    private val weatherRepository: WeatherRepository by inject(WeatherRepository::class.java)

    override suspend fun doWork(): Result {
        return try {
            val (lat, lon, cityName) = weatherRepository.getLastLocation()
            val result = weatherRepository.getCurrentWeather(lat, lon)
            result.onSuccess { weather ->
                WeatherNotificationHelper.showNotification(
                    applicationContext,
                    "${weather.cityName}: ${weather.temperature}°C, ${weather.weatherDescription}"
                )
            }
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }

    companion object {
        const val WORK_NAME = "weather_update_work"
    }
} 