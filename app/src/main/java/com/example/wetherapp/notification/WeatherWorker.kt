package com.example.wetherapp.notification

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.wetherapp.domain.repository.WeatherRepository
import com.example.wetherapp.util.Constants
import org.koin.java.KoinJavaComponent.inject

class WeatherWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    private val weatherRepository: WeatherRepository by inject(WeatherRepository::class.java)

    override suspend fun doWork(): Result {
        return try {
            val (lat, lon, cityName) = weatherRepository.getLastLocation()

            if (lat == Constants.DEFAULT_LAT && lon == Constants.DEFAULT_LON) {
                return Result.failure()
            }
            
            val result = weatherRepository.getCurrentWeather(lat, lon)
            when {
                result.isSuccess -> {
                    result.getOrNull()?.let { weather ->
                        WeatherNotificationHelper.showNotification(applicationContext, weather)
                    }
                    Result.success()
                }
                else -> Result.failure()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }

    companion object {
        const val WORK_NAME = "weather_update_work"
    }
} 