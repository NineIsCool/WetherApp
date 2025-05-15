package com.example.wetherapp.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.wetherapp.data.local.dao.ForecastDao
import com.example.wetherapp.data.local.dao.WeatherDao
import com.example.wetherapp.data.local.entity.ForecastEntity
import com.example.wetherapp.data.local.entity.WeatherEntity
import com.example.wetherapp.data.remote.WeatherApi
import com.example.wetherapp.data.remote.model.ForecastResponse
import com.example.wetherapp.data.remote.model.WeatherResponse
import com.example.wetherapp.domain.model.Forecast
import com.example.wetherapp.domain.model.Weather
import com.example.wetherapp.domain.repository.WeatherRepository
import com.example.wetherapp.util.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class WeatherRepositoryImpl(
    private val weatherApi: WeatherApi,
    private val weatherDao: WeatherDao,
    private val forecastDao: ForecastDao,
    private val dataStore: DataStore<Preferences>
) : WeatherRepository {

    private val latKey = doublePreferencesKey(Constants.PREF_KEY_LAST_LOCATION_LAT)
    private val lonKey = doublePreferencesKey(Constants.PREF_KEY_LAST_LOCATION_LON)
    private val cityNameKey = stringPreferencesKey(Constants.PREF_KEY_LAST_CITY_NAME)

    override suspend fun getCurrentWeather(lat: Double, lon: Double): Result<Weather> {
        return try {
            val response = weatherApi.getCurrentWeather(
                lat = lat,
                lon = lon,
                apiKey = Constants.API_KEY,
                units = Constants.METRIC_UNIT
            )
            val weatherEntity = response.toWeatherEntity()
            weatherDao.insertWeather(weatherEntity)
            Result.success(weatherEntity.toDomainModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCurrentWeatherByCity(cityName: String): Result<Weather> {
        return try {
            val response = weatherApi.getCurrentWeatherByCity(
                cityName = cityName,
                apiKey = Constants.API_KEY,
                units = Constants.METRIC_UNIT
            )
            val weatherEntity = response.toWeatherEntity()
            weatherDao.insertWeather(weatherEntity)
            Result.success(weatherEntity.toDomainModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getForecast(lat: Double, lon: Double): Result<List<Forecast>> {
        return try {
            val response = weatherApi.getForecast(
                lat = lat,
                lon = lon,
                apiKey = Constants.API_KEY,
                units = Constants.METRIC_UNIT
            )
            val forecastEntities = response.toForecastEntities()
            forecastDao.insertForecasts(forecastEntities)
            Result.success(forecastEntities.map { it.toDomainModel() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getForecastByCity(cityName: String): Result<List<Forecast>> {
        return try {
            val response = weatherApi.getForecastByCity(
                cityName = cityName,
                apiKey = Constants.API_KEY,
                units = Constants.METRIC_UNIT
            )
            val forecastEntities = response.toForecastEntities()
            forecastDao.insertForecasts(forecastEntities)
            Result.success(forecastEntities.map { it.toDomainModel() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getLocalWeather(): Flow<Weather?> {
        return weatherDao.getLatestWeather().map { it?.toDomainModel() }
    }

    override fun getLocalWeatherByCity(cityName: String): Flow<Weather?> {
        return weatherDao.getWeatherByCity(cityName).map { it?.toDomainModel() }
    }

    override fun getLocalForecasts(): Flow<List<Forecast>> {
        return forecastDao.getLatestForecasts().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getLocalForecastsByCity(cityName: String): Flow<List<Forecast>> {
        return forecastDao.getForecastsByCity(cityName).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun saveLastLocation(lat: Double, lon: Double, cityName: String) {
        dataStore.edit { preferences ->
            preferences[latKey] = lat
            preferences[lonKey] = lon
            preferences[cityNameKey] = cityName
        }
    }

    override suspend fun getLastLocation(): Triple<Double, Double, String> {
        val preferences = dataStore.data.firstOrNull()
        val lat = preferences?.get(latKey) ?: Constants.DEFAULT_LAT
        val lon = preferences?.get(lonKey) ?: Constants.DEFAULT_LON
        val cityName = preferences?.get(cityNameKey) ?: Constants.DEFAULT_CITY
        return Triple(lat, lon, cityName)
    }

    private fun WeatherResponse.toWeatherEntity(): WeatherEntity {
        return WeatherEntity(
            id = dt,
            cityName = name,
            countryCode = sys.country,
            lat = coord.lat,
            lon = coord.lon,
            temperature = main.temp,
            feelsLike = main.feelsLike,
            tempMin = main.tempMin,
            tempMax = main.tempMax,
            pressure = main.pressure,
            humidity = main.humidity,
            windSpeed = wind.speed,
            windDeg = wind.deg,
            weatherMain = weather.firstOrNull()?.main ?: "",
            weatherDescription = weather.firstOrNull()?.description ?: "",
            weatherIcon = weather.firstOrNull()?.icon ?: "",
            visibility = visibility,
            dt = dt,
            timezone = timezone,
            sunrise = sys.sunrise,
            sunset = sys.sunset
        )
    }

    private fun ForecastResponse.toForecastEntities(): List<ForecastEntity> {
        return list.map { item ->
            ForecastEntity(
                id = item.dtTxt,
                cityId = city.id,
                cityName = city.name,
                dt = item.dt,
                temperature = item.main.temp,
                feelsLike = item.main.feelsLike,
                tempMin = item.main.tempMin,
                tempMax = item.main.tempMax,
                pressure = item.main.pressure,
                humidity = item.main.humidity,
                windSpeed = item.wind.speed,
                windDeg = item.wind.deg,
                weatherMain = item.weather.firstOrNull()?.main ?: "",
                weatherDescription = item.weather.firstOrNull()?.description ?: "",
                weatherIcon = item.weather.firstOrNull()?.icon ?: "",
                dtTxt = item.dtTxt,
                timestamp = System.currentTimeMillis()
            )
        }
    }

    private fun WeatherEntity.toDomainModel(): Weather {
        return Weather(
            id = id,
            cityName = cityName,
            countryCode = countryCode,
            lat = lat,
            lon = lon,
            temperature = temperature,
            feelsLike = feelsLike,
            tempMin = tempMin,
            tempMax = tempMax,
            pressure = pressure,
            humidity = humidity,
            windSpeed = windSpeed,
            windDeg = windDeg,
            weatherMain = weatherMain,
            weatherDescription = weatherDescription,
            weatherIcon = weatherIcon,
            visibility = visibility,
            dt = dt,
            timezone = timezone,
            sunrise = sunrise,
            sunset = sunset
        )
    }

    private fun ForecastEntity.toDomainModel(): Forecast {
        return Forecast(
            id = id,
            cityId = cityId,
            cityName = cityName,
            dt = dt,
            temperature = temperature,
            feelsLike = feelsLike,
            tempMin = tempMin,
            tempMax = tempMax,
            pressure = pressure,
            humidity = humidity,
            windSpeed = windSpeed,
            windDeg = windDeg,
            weatherMain = weatherMain,
            weatherDescription = weatherDescription,
            weatherIcon = weatherIcon,
            dtTxt = dtTxt
        )
    }
} 