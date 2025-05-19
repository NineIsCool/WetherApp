package com.example.wetherapp.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wetherapp.domain.repository.WeatherRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    @OptIn(FlowPreview::class)
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        viewModelScope.launch {
            _searchQuery
                .debounce(300)
                .collect { query ->
                    if (query.isNotBlank() && query.length >= 3) {
                        searchCity(query)
                    }
                }
        }
    }

    fun searchCity(cityName: String) {
        if (cityName.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSearching = true, error = null) }
            try {
                val result = repository.getCurrentWeatherByCity(cityName)
                result.onSuccess { weather ->
                    _uiState.update {
                        it.copy(
                            isSearching = false,
                            searchResult = weather.cityName,
                            lat = weather.lat,
                            lon = weather.lon
                        )
                    }
                }.onFailure { error ->
                    _uiState.update { it.copy(isSearching = false, error = error.message) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isSearching = false, error = e.message) }
            }
        }
    }

    fun resetSearch() {
        _searchQuery.value = ""
        _uiState.update {
            SearchUiState()
        }
    }
}

data class SearchUiState(
    val isSearching: Boolean = false,
    val searchResult: String? = null,
    val lat: Double? = null,
    val lon: Double? = null,
    val error: String? = null
) 