package com.example.wetherapp.presentation.forecast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wetherapp.domain.model.Forecast
import com.example.wetherapp.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ForecastViewModel(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ForecastUiState())
    val uiState: StateFlow<ForecastUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getLocalForecasts().collectLatest { forecasts ->
                _uiState.update { it.copy(forecasts = forecasts) }
            }
        }
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            val (lat, lon, cityName) = repository.getLastLocation()
            fetchForecastByCoordinates(lat, lon)
        }
    }

    fun fetchForecastByCoordinates(lat: Double, lon: Double) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val result = repository.getForecast(lat, lon)
                result.onSuccess { forecasts ->
                    _uiState.update { it.copy(isLoading = false, forecasts = forecasts) }
                }.onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun fetchForecastByCity(cityName: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val result = repository.getForecastByCity(cityName)
                result.onSuccess { forecasts ->
                    _uiState.update { it.copy(isLoading = false, forecasts = forecasts) }
                }.onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun retry() {
        loadInitialData()
    }
}

data class ForecastUiState(
    val isLoading: Boolean = false,
    val forecasts: List<Forecast> = emptyList(),
    val error: String? = null
) 