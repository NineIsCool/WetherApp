package com.example.wetherapp.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.wetherapp.data.local.WeatherDatabase
import com.example.wetherapp.util.Constants
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single { provideDatabase(androidContext()) }
    single { provideWeatherDao(get()) }
    single { provideForecastDao(get()) }
}

private fun provideDatabase(context: Context) =
    Room.databaseBuilder(
        context,
        WeatherDatabase::class.java,
        Constants.DATABASE_NAME
    )
    .fallbackToDestructiveMigration()
    .addCallback(object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
        }

        override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
            super.onDestructiveMigration(db)
        }
    })
    .build()

private fun provideWeatherDao(database: WeatherDatabase) = database.weatherDao()

private fun provideForecastDao(database: WeatherDatabase) = database.forecastDao() 