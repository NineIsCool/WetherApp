package com.example.wetherapp

import android.app.Application
import android.util.Log
import com.example.wetherapp.di.appModule
import com.example.wetherapp.di.databaseModule
import com.example.wetherapp.di.networkModule
import com.example.wetherapp.di.repositoryModule
import com.example.wetherapp.di.viewModelModule
import com.example.wetherapp.util.Constants
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import java.io.File

class WeatherApp : Application() {
    override fun onCreate() {
        super.onCreate()
        clearDatabaseFiles()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@WeatherApp)
            modules(
                listOf(
                    appModule,
                    networkModule,
                    databaseModule,
                    repositoryModule,
                    viewModelModule
                )
            )
        }
    }

    private fun clearDatabaseFiles() {
        try {
            val dbFile = getDatabasePath(Constants.DATABASE_NAME)
            if (dbFile.exists()) {
                dbFile.delete()
                Log.d("WeatherApp", "Database file deleted successfully")

                val journalFile = File(dbFile.path + "-journal")
                if (journalFile.exists()) {
                    journalFile.delete()
                    Log.d("WeatherApp", "Database journal file deleted successfully")
                }

                val shmFile = File(dbFile.path + "-shm")
                if (shmFile.exists()) {
                    shmFile.delete()
                    Log.d("WeatherApp", "Database shm file deleted successfully")
                }

                val walFile = File(dbFile.path + "-wal")
                if (walFile.exists()) {
                    walFile.delete()
                    Log.d("WeatherApp", "Database wal file deleted successfully")
                }
            }
        } catch (e: Exception) {
            Log.e("WeatherApp", "Error clearing database files", e)
        }
    }
} 