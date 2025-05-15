package com.example.wetherapp.util

object Constants {
    const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    const val API_KEY = "67191473c48de2173957667ffe35f6e1"
    const val METRIC_UNIT = "metric"
    const val DATABASE_NAME = "weather_database"
    const val PREFERENCES_NAME = "weather_preferences"

    const val PREF_KEY_LAST_LOCATION_LAT = "last_location_lat"
    const val PREF_KEY_LAST_LOCATION_LON = "last_location_lon"
    const val PREF_KEY_LAST_CITY_NAME = "last_city_name"

    const val DEFAULT_LAT = 51.5074 // London
    const val DEFAULT_LON = -0.1278 // London
    const val DEFAULT_CITY = "London"
} 